window.ru_gzpn_spc_csl_ui_js_Flot = function() {
  var element = $(this.getElement());
  element.height = 300;
  element.width = 400;
  
  d = this.getState().series;
  var opt = {
		  xaxis: {
				mode: "time",
				timeformat: "%Y/%m/%d",
				tickLength: 5,
				tickFormatter: function (val, axis) {
				    var d = new Date(val);
				    return "Date";//d.getUTCDate() + "/" + (d.getUTCMonth() + 1);
				}
			},
			grid: {
				backgroundColor: { colors: [ "#fff", "#eee" ] },
				borderWidth: {
					top: 1,
					right: 1,
					bottom: 2,
					left: 2
				}
			}
		};
  
  this.onStateChange = function() {
    $.plotAnimator(element, [{
    	data: d,
    	options: opt,
    	color: "#0079C2",
    	//highlightColor: "#fff",
    	//points: { show: true },
    	lines: {show: true, fill: true},
		label: "Labe 1",
		
		selection: {
			mode: "x"
		},
		
    	animator: {start: 100, steps: 150, duration: 1600, direction: "right"},
    	
    	
    }]); 
  }
}