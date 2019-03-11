window.ru_gzpn_spc_csl_ui_js_bpmnio_BpmnViewer = function() {
  var element = $(this.getElement());
  
  
  
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
    // import diagram
    bpmnViewer.importXML(bpmnXML, function(err) {
      if (err) {
        return console.error('could not import BPMN 2.0 diagram', err);
      }
      // access modeler components
      var canvas = bpmnViewer.get('canvas');
      var overlays = bpmnViewer.get('overlays');
      var elementRegistry = bpmnViewer.get('elementRegistry');
      var shape = elementRegistry.get(bpmElementId);
      
      var eventBus = bpmnViewer.get('eventBus');
      var events = [
    	  'element.hover',
    	  'element.out',
    	  'element.click',
    	  'element.dblclick',
    	  'element.mousedown',
    	  'element.mouseup'
    	];

      events.forEach(function(event) {
    		eventBus.on(event, function(e) {
    			// e.element = the model element
    			// e.gfx = the graphical element

    			log(event, 'on', e.element.id);
    		});
      });

      // zoom to fit full viewport
     // canvas.zoom('fit-viewport');   
    });
  }

  this.setElementsStatuses = function(elementsInfoArray) {
	  var canvas = bpmnViewer.get('canvas');
      var overlays = bpmnViewer.get('overlays');
      var elementRegistry = bpmnViewer.get('elementRegistry');
      var shape = elementRegistry.get(bpmElementId);
      
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
      var shape = elementRegistry.get(bpmElementId);
      
      var $overlayHtml =
  	    $('<div class="' + cssClass + '">')
  	      .css({
  	        width: shape.width,
  	        height: shape.height
  	      });
      
      return overlays.add(elementId, {
  	    	position: {
  	    		top: 0,
  	    		left: 0
  	    	},
  	    	html: $overlayHtml
  	  });
  }
  
  this.onStateChange = function() {
	  this.openDiagram(this.getState().bpmnXML);
	  this.setElementsStatuses(this.getState().elementInfos);
  }
  
  this.registerRpc({
	  
	  highlight: function(bpmElementId, info) {
		  
		  if (previousHighlitedElementId) {
			  overlays.remove(previousHighlitedElementId);
		  }
		  var canvas = bpmnViewer.get('canvas');
	      var overlays = bpmnViewer.get('overlays');
	      var elementRegistry = bpmnViewer.get('elementRegistry');
	      
	      previousHighlitedElementId = overlays
	      		.add(bpmElementId, 'note', {
	      				position: {
	      					bottom: 0,
	      					right: 0
	      				},
	      				html: '<div class="diagram-note">'
	      					+ '<b>User:&nbsp</b>' + info.user + '</br>'
	      					+ '<b>Status:&nbsp</b>' + info.status + '</br>'
	      					+ '<b>Info:&nbsp</b>' + info.comment 
	      					+ '</div>'
	      });
	      
	      //add marker
	  	  canvas.addMarker(bpmElementId, 'needs-discussion');
	  }
  });
  
}