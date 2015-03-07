$(document).ready(function(){
	$("#popupForSearch").hide();
	$("#popupForEdit").hide();
	$("#popupForInsert").hide();
	$("#popupForDelete").hide();
});

$(function() {
	$( "#popupForSearch" ).dialog({
	autoOpen: false,
	show: {
		effect: "blind",
		duration: 1000
	},
	hide: {
		effect: "blind",
		duration: 1000
	}
	});
	$( "#search" ).click(function() {
	$( "#popupForSearch" ).dialog( "open" );
	});
});

$(function() {
	$( "#popupForEdit" ).dialog({
	autoOpen: false,
	show: {
		effect: "blind",
		duration: 1000
	},
	hide: {
		effect: "blind",
		duration: 1000
	}
	});
	$( "#edit" ).click(function() {
	$( "#popupForEdit" ).dialog( "open" );
	});
});

$(function() {
	$( "#popupForInsert" ).dialog({
	autoOpen: false,
	show: {
		effect: "blind",
		duration: 1000
	},
	hide: {
		effect: "blind",
		duration: 1000
	}
	});
	$( "#insert" ).click(function() {
	$( "#popupForInsert" ).dialog( "open" );
	});
});

$(function() {
	$( "#popupForDelete" ).dialog({
	autoOpen: false,
	show: {
		effect: "blind",
		duration: 1000
	},
	hide: {
		effect: "blind",
		duration: 1000
	}
	});
	$( "#delete" ).click(function() {
	$( "#popupForDelete" ).dialog( "open" );
	});
});