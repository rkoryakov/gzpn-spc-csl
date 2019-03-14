window.ru_gzpn_spc_csl_ui_js_bpmnio_viewer_BpmnViewer = function() {
  var element = $(this.getElement());
  var rpcProxy = this.getRpcProxy();
  
  
  // viewer instance
  var bpmnViewer = new BpmnJS({
    container: element
  });
  
  /**
   * Open diagram in our modeler instance.
   *
   * @param {String} bpmnXML diagram to display
   */
  this.openDiagram = function (bpmnXML) {
	var elementInfos = this.getState().elementInfos;
	var thisScope = this;
    // import diagram
    bpmnViewer.importXML(bpmnXML, function(err) {
      if (err) {
        return console.error('could not import BPMN 2.0 diagram', err);
      }
      // access modeler components
      var canvas = bpmnViewer.get('canvas');
      var overlays = bpmnViewer.get('overlays');
      var elementRegistry = bpmnViewer.get('elementRegistry');
      
      var eventBus = bpmnViewer.get('eventBus');
      var events = [
    	  'element.hover',
    	//  'element.out',
    	  'element.click'
    	 // 'element.dblclick',
    	 // 'element.mousedown',
    	//  'element.mouseup'
    	];

      events.forEach(function(event) {
    		eventBus.on(event, function(e) {
    			// e.element = the model element
    			// e.gfx = the graphical element
    			if (event == 'element.hover') {
    				rpcProxy.onElementOver(e.element.id);
    			} else 
    			if (event == 'element.click') {
    				rpcProxy.onElementClick(e.element.id);
    			}
    			
    			//log(event, 'on', e.element.id);
    		});
      });
      
      thisScope.setElementsStatuses(elementInfos);
      
      // zoom to fit full viewport
      canvas.zoom('fit-viewport');   
    });
  }

  this.setElementsStatuses = function(elementsInfoArray) {
	  var canvas = bpmnViewer.get('canvas');
      var overlays = bpmnViewer.get('overlays');
      var elementRegistry = bpmnViewer.get('elementRegistry');
      
	  for (i = 0; i < elementsInfoArray.length; i ++) {
		  elementId = elementsInfoArray[i].elementId;
		  user = elementsInfoArray[i].user;
		  role = elementsInfoArray[i].role;
		  status = elementsInfoArray[i].status;
		  comment = elementsInfoArray[i].comment;
		  isActive = elementsInfoArray[i].isActive;
		  isCompleted = elementsInfoArray[i].isCompleted;
		  
		  if (isActive) {
			  this.addOverlay(elementId, 'active-overlay');
		  } else if (isCompleted) {
			  this.addOverlay(elementId, 'completed-overlay');
		  } else {
			  this.addOverlay(elementId, 'created-overlay');
		  }
	  }
  }
  
  this.addOverlay = function(elementId, cssClass) {
	  var canvas = bpmnViewer.get('canvas');
      var overlays = bpmnViewer.get('overlays');
      var elementRegistry = bpmnViewer.get('elementRegistry');
      var shape = elementRegistry.get(elementId);
      
      var $overlayHtml =
  	    $('<div class="' + cssClass + '">')
  	      .css({
  	        width: shape.width,
  	        height: shape.height
  	      });
      
      overlays.add(elementId, {
  	    	position: {
  	    		top: 0,
  	    		left: 0
  	    	},
  	    	html: $overlayHtml
  	  });
      //add marker
  	  canvas.addMarker(elementId, 'needs-discussion');
  }
  
  this.onStateChange = function() {
	  this.openDiagram(this.getState().bpmnXML);
  }
  
  var previousHighlitedElementId;
  
  var thisScope = this;
  this.registerRpc({
	  
	  highlight: function(bpmElementId, info) {
		  
		  var canvas = bpmnViewer.get('canvas');
	      var overlays = bpmnViewer.get('overlays');
	      var elementRegistry = bpmnViewer.get('elementRegistry');
	      
	      if (previousHighlitedElementId) {
			  overlays.remove(previousHighlitedElementId);
		  }
	      
	      previousHighlitedElementId = overlays
	      		.add(bpmElementId, 'note', {
	      				position: {
	      					bottom: 0,
	      					right: 0
	      				},
	      				html: '<div class="diagram-note">'
	      					+ '<b>Ответственный:&nbsp</b>' + (info.user == null ? 'Не назначен' : info.user) + '</br>'
	      					+ '<b>Статус:&nbsp</b>' + (info.status == null ? 'Не задан' : info.status) + '</br>'
	      					+ '<b>Дата создания:&nbsp</b>' + (info.createDate == null ? ' - ' : thisScope.formatDate(info.createDate)) + '</br>'
	      					+ '<b>Дата открытия:&nbsp</b>' + (info.openDate == null ? ' - ' : thisScope.formatDate(info.openDate)) + '</br>'
	      					+ '<b>Дата закрытия:&nbsp</b>' + (info.closeDate == null ? ' - ' : thisScope.formatDate(info.closeDate)) + '</br>'
	      					+ '<b>Информация:&nbsp</b>' + (info.comment == null ? 'Отсутствует' : info.comment) + '</br>'
	      					+ '</div>'
	      });
	      
	      //add marker
	  	  canvas.addMarker(bpmElementId, 'needs-discussion');
	  }
  });
  
  this.formatDate = function(oDate) {
	  date = new Date(oDate);
	  dayOfMonth = date.getDate();
	  month = date.getMonth() + 1;
	  year = date.getFullYear();
	  hour = date.getHours();
	  minutes = date.getMinutes();
	  seconds = date.getSeconds();
	  
	  // formatting
	  year = year.toString().slice(-2);
	  month = month < 10 ? '0' + month : month;
	  dayOfMonth = dayOfMonth < 10 ? '0' + dayOfMonth : dayOfMonth;
	  seconds = seconds < 10 ? '0' + seconds : seconds;
	  minutes = minutes < 10 ? '0' + minutes : minutes;
	  hour = hour < 10 ? '0' + hour : hour;
	  
	  return dayOfMonth + '.' + month + '.' + year + ' ' + hour + ':' + minutes + ':' + seconds;
  }
  
}