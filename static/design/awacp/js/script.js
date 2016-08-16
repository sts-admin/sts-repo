function ShowHide(val)
{
	var dis = $('#'+val).css('display');

	if(dis == 'none')
	{
		$('#'+val).show();
		$('#'+val+'_div').show();
	}
	else
	{
		$('#'+val).hide();
		$('#'+val+'_div').hide();
	}
}