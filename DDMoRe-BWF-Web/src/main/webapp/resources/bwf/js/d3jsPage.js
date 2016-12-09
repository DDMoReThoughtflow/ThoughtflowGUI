/**
 * Insert this file, if you want to show d3js chart.
 */

var w = 1000,
    h = 600,
    linkDistance = 200;

var svg = d3
	.select("#chart")
	.append("svg")
	.attr({ "width": w, "height": h });

var tip = d3
	.tip()
	.attr('class', 'd3-tip')
	.offset([-10, 0])
	.html(function(d) {
		var tStr = '<span style="color:grey;">' + d.name +  ': </span><span><strong>' + d.type + '</strong></span>';
		
		var addStr = '';
		var added = false;
		if (!isUndefined(d.location)) {
			addStr += '<span style="color:grey;">' + d.location + '</span>';
			added = true;
		}
		if (!isUndefined(d.label)) {
			addStr += '<span style="color:grey;">' + d.label + '</span>';
			added = true;
		}
		if (!isUndefined(d.description)) {
			if (added) {
				addStr += '<br />';
			}
			addStr += '<span style="color:grey;">' + d.description + '</span>';
			added = true;
		}
		if (!isUndefined(d.note)) {
			if (added) {
				addStr += '<br />';
			}
			addStr += '<span style="color:grey;">' + d.note + '</span>';
			added = true;
		}
		if (!isUndefined(d.startTime)) {
			if (added) {
				addStr += '<br />';
			}
			addStr += '<span style="color:grey;">Start: ' + new Date(d.startTime).toLocaleString() + '</span>';
			added = true;
		}
		if (!isUndefined(d.endTime)) {
			if (added) {
				addStr += '<br />';
			}
			addStr += '<span style="color:grey;">End: ' + new Date(d.endTime).toLocaleString() + '</span>';
			added = true;
		}
		if (added) {
			tStr += '<hr style="margin:5px 0 5px 0;" />';
			tStr += addStr;
		}

		return tStr;
	});

var force = d3
	.layout.force()
    .nodes(dataset.nodes)
    .links(dataset.edges)
    .size([w,h])
    .linkDistance([linkDistance])
    .charge([-500])
    .theta(0.1)
    .gravity(0.05)
    .start();

var edges = svg
	.selectAll("line")
	.data(dataset.edges)
    .enter()
    .append("line")
    .attr("id", function(d,i) { return 'edge' +i })
    .attr('marker-end', 'url(#arrowhead)')
    .style("stroke", "#000") // edge line color
    .style("pointer-events", "none");

var nodes = svg
	.selectAll("circle")
    .data(dataset.nodes)
    .enter()
    .append("circle")
    .attr({ 
    	"fill": function(d, i) { 
    				var nodeType = d.type.toLowerCase();
    				if ("activity" === nodeType) {
    					return "#ffe124";
    				} else if ("agent" === nodeType) {
    					return "#666";
    				} else if ("entity" === nodeType) {
    					return "#59a003";
    				}
    			}, 
    	"r": 15, 
    	"class": function(d, i) { return "node " + d.type.toLowerCase(); }
    })
    //.style("fill", function(d, i) { return colors(i); })
	.on('mouseover', tip.show)
    .on('mouseout', tip.hide)
    .on("click", function(d) { onClickCircle(d) })
    .call(force.drag)
    
var nodelabels = svg
	.selectAll(".nodelabel") 
    .data(dataset.nodes)
    .enter()
    .append("text")
    .attr({"x": function(d) { return d.x; },
           "y": function(d) { return d.y; },
           "class": "nodelabel",
           "stroke": "black",
           "font-size": 12})
   .text(function(d) {
	   if ("activity" === d.type.toLowerCase()) {
		   return d.label;		   
	   } else {
		   return d.name;   
	   }
   });

var edgepaths = svg
	.selectAll(".edgepath")
	.data(dataset.edges)
    .enter()
    .append('path')
    .attr({'d': function(d) {return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y},
           'class': 'edgepath',
           'fill-opacity': 0,
           'stroke-opacity': 0,
           'fill': 'blue',
           'stroke': 'red',
           'id': function(d, i) { return 'edgepath' + i }})
    .style("pointer-events", "none");

var edgelabels = svg
	.selectAll(".edgelabel")
	.data(dataset.edges)
    .enter()
    .append('text')
    .style("pointer-events", "none")
    .attr({'class': 'edgelabel',
           'id': function(d, i) { return 'edgelabel' + i },
           'dx': 80, // Position for path label
           'dy': 12, // Position for path label
           'font-size': 12,
           'fill': '#000'}); // label color

edgelabels
	.append('textPath')
    .attr('xlink:href', function(d, i) { return '#edgepath' + i })
    .style("pointer-events", "none")
    .text(function(d, i) { return d.label });

svg
	.append('defs')
	.append('marker')
    .attr({'id': 'arrowhead',
           'viewBox': '-0 -5 10 10',
           'refX': 25,
           'refY': 0,
           //'markerUnits':'strokeWidth',
           'orient': 'auto',
           'markerWidth': 10,
           'markerHeight': 10,
           'xoverflow': 'visible'})
    .append('svg:path')
    	.attr('d', 'M 0,-5 L 10 ,0 L 0,5')
        .attr('fill', '#000')   // arrow color
        .attr('stroke','#000'); // arrow color

svg.call(tip);
        
force.on("tick", function() {
	
    edges.attr({"x1": function(d) { return d.source.x; },
                "y1": function(d) { return d.source.y; },
                "x2": function(d) { return d.target.x; },
                "y2": function(d) { return d.target.y; }
	});

    nodes.attr({"cx": function(d) { return d.x; },
                "cy": function(d) { return d.y; }
    });

    nodelabels.attr("x", function(d) { return d.x + 20; }) // Position for node label
              .attr("y", function(d) { return d.y; });     // Position for node label

    edgepaths.attr('d', function(d) {
    	var path = 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y;
        //console.log(d)
        return path
    });       

	edgelabels.attr('transform', function(d, i) {
    	if (d.target.x < d.source.x) {
        	bbox = this.getBBox();
            rx = bbox.x + bbox.width / 2;
            ry = bbox.y + bbox.height / 2;
            return 'rotate(180 ' + rx + ' ' + ry + ')';
		} else {
			return 'rotate(0)';
		}
    });
	
});