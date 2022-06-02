$().ready(function () {
$("#imagenEliminar").click(function () {
	$("#eliminarImagen").val("S");
	$("#imagenGuardada").remove();
	$("#imagenEliminar").remove();
});
});
