$(document).ready(function(){
	$("#popupForSearch").hide();
})

function searchSplash(){
	console.log("Searching Screen initiated.");
	$("#popupForSearch").show();
	$("#popupForSearch").dialog();
}

function editSplash(){
	console.log("Editing Screen initiated.");
}

function deleteSplash(){
	console.log("Deletion Screen initiated.");
}

function insertSplash(){
	console.log("Insertion Screen initiated.");
}