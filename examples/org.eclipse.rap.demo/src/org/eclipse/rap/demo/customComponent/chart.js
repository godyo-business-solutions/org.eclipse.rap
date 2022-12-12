
console.log("script loaded")

function createDivElement(parent) {
	const element = document.createElement("div");
	element.classList.add("custom-element")
	parent.append(element);
	return element;
}

function selectOrAppend(parent, child) {
	const select = parent.select(child)
	return select.size() ? select : parent.append(child)
}

function transformSelected(shiftRadius, d) {
	const halfAngle = (d.startAngle + d.endAngle) / 2
	const dX = shiftRadius * Math.sin(halfAngle)
	const dY = - shiftRadius * Math.cos(halfAngle)
	return `--dX: ${dX}px; --dY: ${dY}px`;
}

class PieChart {

	constructor(parent) {
		this.parent = parent
		this.element = createDivElement(parent)

		this.svg = d3.select(this.element).append("svg")
		this.chartgroup = this.svg.append("g")
		this.legendgroup = this.svg.append("g")

		this.data = []

		rap.on("render", () => this.render())
	}

	render() {
		if (!this.needsRender) {
			return;
		}
		this.needsRender = false;

		const radius = 90;
		const shiftRadius = 13;
		const padX = 15;
		const padY = 15;

		const data_ready = d3.pie().value(d => d.value).sort(null)(this.data)
		console.log("data_ready:", data_ready)

		this.chartgroup.attr("transform", `translate(${radius + padX}, ${radius + padY})`);

		// gradients
		this.svg.selectAll("radialGradient")
			.data(data_ready)
			.join(
				enter => {
					const grad = enter.append("radialGradient")
						.attr("id", d => `grad-${d.index}`)
						.attr("cx", 0)
						.attr("cy", 0)
						.attr("r", radius)
						.attr("gradientUnits", "userSpaceOnUse")

					grad.append("stop")
						.attr("offset", "0%")
						.attr("stop-color", d => d.data.color)
						.attr("stop-opacity", 0.5)
					grad.append("stop")
						.attr("offset", "100%")
						.attr("stop-color", d => d.data.color)
						.attr("stop-opacity", 1)
				},
				update => {
					update.selectAll("stop").attr("stop-color", d => d.data.color)
				}
			);

		// chart - sectors
		this.chartgroup.selectAll("path")
			.data(data_ready)
			.join('path')
			.on('click', (_, { index }) => {
				this.notifySelected(index)
				this.chartgroup.selectAll("path").data(data_ready).classed("selected", d => d.index === index);
			})
			.classed("pie-sector", true)
			.attr('fill', d => `url(#grad-${d.index}`)
			.attr('style', d => transformSelected(shiftRadius, d))
			.transition()
			.attr('d', d3.arc().innerRadius(0).outerRadius(radius));

		const lineheight = 25;
		const legendItemRadius = 5;
		const legendX = 30;
		const legendSeperation = 10;
		const textHeight = 5;

		//legend
		const legendGroups = this.legendgroup.selectAll("g")
			.data(data_ready)
			.join("g")
			.attr("transform", d => `translate(${2 * radius + legendX + padX}, ${(d.index - (data_ready.length - 1) / 2) * lineheight + radius + padY})`);

		// legend icons 
		selectOrAppend(legendGroups, "circle")
			.attr('cx', 0)
			.attr('cy', 0)
			.attr('r', legendItemRadius)
			.attr('fill', d => d.data.color)

		// legend labels
		selectOrAppend(legendGroups, "text")
			.attr('x', legendSeperation)
			.attr('y', textHeight)
			.text(d => d.data.label)

	}

	notifySelected(index) {
		const remoteObject = rap.getRemoteObject(this);
		remoteObject.notify("Selection", { index });
	}

	setItems(data) {
		this.data = data;
		this.needsRender = true;
	}

	destroy() {
		this.element.parentNode?.removeChild(element);
	}
}

rap.registerTypeHandler("rwt.chart.PieChart", {

	factory(properties) {
		// clientSideObject
		const parent = rap.getObject(properties.parent);
		return new PieChart(parent);
	},

	destructor: "destroy",

	properties: ["items"],

	methods: [],

	events: ["Selection"]

});