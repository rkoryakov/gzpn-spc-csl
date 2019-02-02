window.ru_gzpn_spc_csl_ui_js_Flot = function() {
  var element = $(this.getElement());
  element.height = 300;
  element.width = 400;
  
  d = this.getState().series;
 
  this.onStateChange = function() {
    $.plotAnimator(element, [{data: d, lines: {show:true, fill:true}, label: "Labe 1", animator: { start: 100, steps: 150, duration: 1000, direction: "right" }}]); 
  }
}