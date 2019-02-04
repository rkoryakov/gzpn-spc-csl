window.ru_gzpn_spc_csl_ui_js_Flot = function() {
  var element = $(this.getElement());
  element.height = 300;
  element.width = 400;
  
  d = this.getState().series;

  this.onStateChange = function() {
    $.plot(element, [{
    	data: d,
    	color: "#0079C2",
    	highlightColor: "#fff",
    	points: { show: true },
    	lines: {show: true, fill: true},
		label: "Labe 1",
		clickable: true,
		selection: {
			mode: "x"
		},
    	
		grow: { growings:[ {
							reanimate: "continue",
							stepMode: "linear",
							stepDirection: "up",
							valueIndex: 1
						   } ] }
    	
    	
    }],
    
    {
    	series: { grow: { active: true, duration: 1300 } },
    	xaxis: {
    
    		mode: "time",
			timeformat: "%Y/%m/%d",
			//tickLength: 4,
			tickFormatter: function (val, axis) {
				var d = new Date(val);
				return d.getUTCDate() + "/" + (d.getUTCMonth() + 1);
			}
    	},
    	
    	grid: {
    		backgroundColor: { colors: [ "#fff", "#eee" ] },
    		hoverable: true,
			clickable: true,
    		borderWidth: {
    			top: 1,
    			right: 1,
    			bottom: 2,
    			left: 2
    		}
    	}
    }); 
  }
  
  
  $("<div id='tooltip'></div>").css({
		position: "absolute",
		display: "none",
		border: "1px solid #fdd",
		padding: "2px",
		"background-color": "#fee",
		opacity: 0.80
	}).appendTo("body");
  
  element.bind("plothover", function (event, pos, item) {

	if (item) {
		var d = new Date(item.datapoint[0]);
		y = item.datapoint[1].toFixed(2);
		x = d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear();
			
			$("#tooltip").html(item.series.label + " of " + x + " = " + y)
					.css({top: item.pageY+5, left: item.pageX+5})
					.fadeIn(200);
			} else {
				$("#tooltip").hide();
			}
  });
  
}