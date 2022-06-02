$().ready(function () {
  $('th').click(function() {
	if(this.getAttribute('class')=='opciones')
		return;
    var table = $(this).parents('table').eq(0)
    var rows = table.find('tr:gt(0)').toArray().sort(comparer($(this).index()))
    this.asc = !this.asc
    if (!this.asc) {
      rows = rows.reverse()
    }
    for (var i = 0; i < rows.length; i++) {
      table.append(rows[i])
    }
    setClass($(this), this.asc);
  })

  function comparer(index) {
    return function(a, b) {
      var valA = getCellValue(a, index),
        valB = getCellValue(b, index)
      return $.isNumeric(valA) && $.isNumeric(valB) ? valA - valB : valA.localeCompare(valB)
    }
  }

  function getCellValue(row, index) {
    return $(row).children('td').eq(index).html()
  }

  function setClass(element, asc) {
    $("th").each(function(index) {
      $(this).removeClass("asc");
      $(this).removeClass("desc");
    });
    if (asc) element.addClass("asc");
    else element.addClass("desc");
  }
});