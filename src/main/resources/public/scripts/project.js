$("#save").onclick = function () {
	var project = buildProject($("#active").value == "true");
	save(project, (id) => {
		if(isNaN(project.id)) {
			ldf.nav("project?id=" + id);
    	}
	});
}

wrapInit("#start").onclick = function () {
	disableButtons();
	var project = buildProject(true);
	save(project);
	restart(() => ldf.locchange());
}

wrapInit("#stop").onclick = function () {
	disableButtons();
	var project = buildProject(false);
	save(project);
	stop(() => ldf.locchange());
}

wrapInit("#restart").onclick = function () {
	disableButtons();
	restart(() => ldf.locchange());
}

$("#runtimeLogs").onclick = function () {
	loadLog("RUN");
}

$("#buildLogs").onclick = function () {
	loadLog("BUILD");
}

function loadLog(type) {
	fetch("/api/project/logs/" + $("#id").value + "?type=" + type)
		.then(r => r.text()
			.then(t => ldf.change("#logDiv", t))
		).catch(console.log);
}

function restart(cb) {
	fetch("/api/project/restart/" + $("#id").value).then(r => {
		console.log(r);
		if(cb) cb();
	}).catch(r => {
		console.log(r);
		if(cb) cb();
	});
}

function stop(cb) {
	fetch("/api/project/stop/" + $("#id").value).then(r => {
    		console.log(r);
    		if(cb) cb();
    	}).catch(r => {
    		console.log(r);
    		if(cb) cb();
    	});
}

function save(project, cb) {
	fetch("/api/project", {method: "POST", body: JSON.stringify(project)})
		.then(r => {
	        r.text().then(t => {
	            if(cb) cb(t);
	        });
	    }).catch(r => {
	        console.log(r);
	        if(cb) cb();
	    });
}

function buildProject(active) {
	return {
			id: parseInt($("#id").value),
    		name: $("#name").value,
    		port: parseInt($("#port").value),
    		gitUrl: $("#gitUrl").value,
    		buildCmd: $("#buildCmd").value,
    		startCmd: $("#startCmd").value,
    		active: active
    	}
}

function wrapInit(query) {
	var node = $(query);
	return node ? node : new Object();
}

function disableButtons() {
	for(input of $$("input")) {
		if(input.type == "button" && input.value != "Save") {
			input.disabled = true;
			input.style.cursor = "not-allowed";
		}
	}
}

if (isNaN(parseInt($("#id").value))) {
	disableButtons();
}