window.ru_gzpn_spc_csl_ui_js_Flot = function() {
  var element = $(this.getElement());

  this.onStateChange = function() {
    $.plot(element, this.getState().series);
  }
}