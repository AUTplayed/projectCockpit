$("#save").onclick = function () {
	var project = buildProject($("#active").value == "true");
	save(project);
}

wrapInit("#start").onclick = function () {
	var project = buildProject(true);
	save(project);
	restart();
}

wrapInit("#stop").onclick = function () {
	var project = buildProject(false);
	save(project);
	stop();
}

wrapInit("#restart").onclick = function () {
	restart();
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

function save(project) {
	fetch("/api/project", {method: "POST", body: JSON.stringify(project)}).then(console.log).catch(console.log);
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