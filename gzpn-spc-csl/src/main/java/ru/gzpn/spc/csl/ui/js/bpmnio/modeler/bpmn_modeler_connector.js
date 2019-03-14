window.ru_gzpn_spc_csl_ui_js_bpmnio_modeler_BpmnModeler = function() {
  var element = $(this.getElement());

  
  var diagramUrl = 'https://cdn.rawgit.com/bpmn-io/bpmn-js-examples/dfceecba/starter/diagram.bpmn';
  // modeler instance
  var bpmnModeler = new BpmnJS({
    container: element
  });
  /**
   * Save diagram contents and print them to the console.
   */
  this.exportDiagram = function () {
    bpmnModeler.saveXML({ format: true }, function(err, xml) {
      if (err) {
        return console.error('could not save BPMN 2.0 diagram', err);
      }
      alert('Diagram exported. Check the developer tools!');
      console.log('DIAGRAM', xml);
    });
  }
  /**
   * Open diagram in our modeler instance.
   *
   * @param {String} bpmnXML diagram to display
   */
  this.openDiagram = function (bpmnXML) {
    // import diagram
    bpmnModeler.importXML(bpmnXML, function(err) {
      if (err) {
        return console.error('could not import BPMN 2.0 diagram', err);
      }
      // access modeler components
      var canvas = bpmnModeler.get('canvas');
      var overlays = bpmnModeler.get('overlays');
      var elementRegistry = bpmnModeler.get('elementRegistry');
      var shape = elementRegistry.get('EstimatesApproval');

//      var $overlayHtml =
//    	    $('<div class="completed-overlay">')
//    	      .css({
//    	        width: shape.width,
//    	        height: shape.height
//    	      });
      
      // zoom to fit full viewport
     // canvas.zoom('fit-viewport');
      // attach an overlay to a node
//      overlays.add('EstimatesApproval', 'note', {
//        position: {
//          bottom: 0,
//          right: 0
//        },
//        html: '<div class="diagram-note">Mixed up the labels?</div>'
//      });
      
//      overlays.add('EstimatesApproval', {
//    	    position: {
//    	      top: 0,
//    	      left: 0
//    	    },
//    	    html: $overlayHtml
//    	  });
//      
//      // add marker
//      canvas.addMarker('EstimatesApproval', 'needs-discussion');
    });
  }
  // load external diagram file via AJAX and open it
  this.onStateChange = function() {
	  this.openDiagram(this.getState().bpmnXML);
	  //$.get(diagramUrl, this.openDiagram, 'text');
	  // wire save button
	  //$('#save-button').click(exportDiagram);
  }
  
  this.registerRpc({
	  deployBpmn: function() {
		  this.exportDiagram();
	  }
  });
  
}