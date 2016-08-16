var base_url = $('#base_url').val();

var timer, timeout = 2000;
var ajaxRunning = false;
		
$(function() {

    $('#login-form-link').click(function(e) {
		$("#login-form").delay(100).fadeIn(100);
 		$("#forgot-form").fadeOut(100);
		$('#forgot-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});
	$('#forgot-form-link').click(function(e) {
		$("#forgot-form").delay(100).fadeIn(100);
 		$("#login-form").fadeOut(100);
		$('#login-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});

});

function setRecPage(val, controller, formname)
{
	var postData = $("#"+formname).serializeArray();
	
	$.ajax(
	{
		url : base_url+'common/setRec',
		type: "POST",
		data : 'val='+ val,
		success:function(data, textStatus, jqXHR) 
		{ 	
			$(".loading_div").show(); /*show loading element*/
			var page = 1; /*get page number from link*/
			$("#contents").load(base_url+controller,{"page":page, "postData":postData}, function(){ //get content from PHP page
				$(".loading_div").hide(); /*once done, hide loading element*/
				
				reloadScripts();
			});	
		}
	});
}

function ClearSearch(controller)
{
	$('.searchtxt').val('');	
	$(".loading_div").show(); /*show loading element*/
	var page = 1; /*get page number from link*/
	$("#contents").load(base_url+controller, function(){ /*get content from PHP page*/
		$(".loading_div").hide(); /*once done, hide loading element*/
		$('.found_row').hide();
		
		reloadScripts();
	});	
}

function takeoff_color(color)
{
	$('#color').val(color);
	
	var postData = $("#takeoff_view_form").serializeArray();
	
	$(".loading_div").show(); /*show loading element*/
	var page = 1; /*get page number from link*/
	$("#contents").load(base_url+'takeoff',{"page":page, "postData":postData}, function(){ /*get content from PHP page*/
		$(".loading_div").hide(); /*once done, hide loading element*/
		
		reloadScripts();
		
		if( color != '' )
			$('.found_row').show();
	});	
}

function joborder_dropdown(final_update)
{
	
	$('#final_update').val(final_update);
	
	var postData = $("#joborder_view_form").serializeArray();
	
	$(".loading_div").show(); /*show loading element*/
	var page = 1; /*get page number from link*/
	$("#contents").load(base_url+'joborder',{"page":page, "postData":postData}, function(){ /*get content from PHP page*/
		$(".loading_div").hide(); /*once done, hide loading element*/
		
		reloadScripts();
		
	});	
}

function reloadScripts()
{
	$('.datepicker').datepicker({'format':'mm/dd/yy'});
	
	$(".switchbox").bootstrapSwitch();
}

function reinitPicker()
{
	if( $('#contents')[0] )
	{
		/* Clear timer if it's set.*/
		if (typeof timer != undefined)
			clearTimeout(timer);
			
		/* Set status to show we're done typing on a delay.*/
		timer = setTimeout(function()
		{
			$(".loading_div").show(); 
			
			if( !ajaxRunning )
			{
				ajaxRunning = true;
				var form_name =  $('#form_name').val();
				var postData = $("#"+form_name).serializeArray();
				var paging_path = $('#paging_path').val();
				
				$("#contents").load(paging_path,{"postData":postData}, function(response, status, xhr){ /*get content from PHP page*/
					if(status == 'success')
					{
						$(".loading_div").hide();
						$('.found_row').show();
						ajaxRunning = false;
						
						reloadScripts();
					}
				});
			
			}
			
		}, timeout);
	}
}

function replaceInput(id, act, prev_value)
{
	if( id != '' )
	{
		if( act == 'new' )
		{
			$('#cntnr_'+id).html('<input type="text" class="form-control" name="'+ id +'" id="'+ id +'" value="" />');
			
			$('#link_'+id).html('REVERT').attr('onclick','replaceInput("'+id+'","old","'+prev_value+'")');
		}
		else if( act == 'old' )
		{
			$.ajax(
			{
				url : base_url+'takeoff/reverSelect',
				type: "POST",
				data : 'act='+ id +'&prev_value='+ prev_value,
				success:function(data, textStatus, jqXHR) 
				{ 	
					$('#cntnr_'+id).html(data);
					$('#link_'+id).html('NEW').attr('onclick','replaceInput("'+id+'","new","'+prev_value+'")');
				}
			});
	
			
		}
	}
}

function disp_confirm(msg, url)
{
	if( confirm(msg) )
		window.location.href = url;
}

function ShowHideForm(formid)
{
	if( $('#'+formid).is(":visible") )
	{
		 $('#'+formid).slideUp('slow');
		 $('#form_display').text('Show Search Form');
	}
	else
	{
		$('#'+formid).slideDown('slow');
		$('#form_display').text('Hide Search Form');
	}
}

function ConfirmMakeQuote(base_url, val, takeoff)
{
	if( confirm('Do you really want to make quote of this takeoff?') )	
		window.location.href = base_url+'quote/makequote/'+val+'/'+takeoff;
}


/* close opened modal*/
function closeModal()
{
	var pre_exist = $('.nicemodal-window')[0];
			
	if(typeof(pre_exist) != 'undefined')
		$(".nicemodal-close-button").trigger('click');
}

function users_mode(mode)
{
	$('#status').val(mode);
	
	var postData = $("#users_view_form").serializeArray();
	
	$(".loading_div").show(); /*show loading element*/
	var page = 1; /*get page number from link*/
	$("#contents").load(base_url+'users',{"page":page, "postData":postData}, function(){ /*get content from PHP page*/
		$(".loading_div").hide(); /*once done, hide loading element*/
		
		if( mode == 'deleted' )
		{
			$('#status_users').text('Active User').addClass('btn-success').removeClass('btn-danger').attr('onclick','users_mode(\'active\')');
		}
		else if( mode == 'active' )
		{
			$('#status_users').text('Deleted User').addClass('btn-danger').removeClass('btn-success').attr('onclick','users_mode(\'deleted\')');
		}
		
		reloadScripts();
	});	
}

function ShowDetail( uid )
{
	var dis = $('#userdetails_'+uid).css('display');

	if(dis == 'none')
		$('#userdetails_'+uid).show();
	else
		$('#userdetails_'+uid).hide();
}

function ModifyUserStatus(status, uid)
{
	if( confirm("Do you really want to mark this user as "+status) )
	{
		$.ajax(
		{
			url : base_url+'users/modifyuserstatus',
			type: "POST",
			data : 'status='+ status +'&uid='+ uid,
			success:function(data, textStatus, jqXHR) 
			{ 	
				if( data.trim() == 'done' )
				{
					$('#user_'+uid).remove();
					$('#userdetails_'+uid).remove();
				}
			}
		});
	}
}

function QuoteRevBid()
{
	var rev_val = $('#in_revision').val();
	
	if(rev_val != '')
	{
		$('.search-choice').remove();
		$('#selected_bidders').val('');
	}	
	
}

function filter_rev()
{
	$('#quote_rev').val('0');	
	
	$(".loading_div").show(); 
			
	if( !ajaxRunning )
	{
		ajaxRunning = true;
		var form_name =  $('#form_name').val();
		var postData = $("#"+form_name).serializeArray();
		var paging_path = $('#paging_path').val();
		
		$("#contents").load(paging_path,{"postData":postData}, function(response, status, xhr){ /*get content from PHP page*/
			if(status == 'success')
			{
				$(".loading_div").hide();
				$('.found_row').show();
				ajaxRunning = false;
				
				reloadScripts();
			}
		});
	
	}
}

function roll_back(orderid)
{
	if(confirm('Are you sure? Do you really want to roll back this job from final updates?'))
	{
		$.ajax({
			type : 'POST',
			url : base_url+'joborder/roll_back',
			data: 'orderid='+ orderid,
			success : function(data){
				if( data == 1 )
				{
					$('#roll_back_'+orderid).remove();
					$('#amount_'+orderid).css('color','blue');
				}
				else
					alert('Request failed. Please try again.');
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				
			}
		});		
	}
}

function ShowHidePayment(val,amount)
{
	if(val == 'PARTIAL')
	{
		$('#partial_span').show();
		$('#in_partial_amount').val('');
	}
	else
	{
		$('#partial_span').hide();
		$('#in_partial_amount').val(Number(amount));
	}
}

function ShowHidePayment2(val,amount)
{
	if(val == 'PARTIAL')
	{
		$('#partial_span2').show();
		$('#in_partial_amount').val('');
	}
	else
	{
		$('#partial_span2').hide();
		$('#in_partial_amount').val(Number(amount));
	}
}

function ChangeInvBoxes(eleid,id)
{
	$('#'+eleid).html('<input type="text" class="form-control" name="'+id+'" id="'+id+'" style="width:760px;" />');
	$('#'+eleid+'_new').html('');
	$('#'+id).focus();
}

function AppendCostSheetRows()
{
	var clone = $('#extra_rows').clone();

	$('#extra_rows').after(clone);
}

function job_final_submit(orderid)
{
	if(confirm('Are you sure? Do you really want to send this job for final updates?'))
	{
		$.ajax({
			type : 'POST',
			url : base_url+'joborder/final_submit',
			data: 'orderid='+ orderid,
			success : function(data){
				
				if( data == 1 )
				{
					$('#final_update').remove();
				}
				else
					alert('Request failed. Please try again.');
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				
			}
		});		
	}
}

function DeleteJobOrder(orderid)
{
	var base_url = $('#base_url').val();
	var r = confirm('Dear Admin, Do you really want to delete this Job Order?');

	if (r == true)
  	{
  		window.location.href = base_url+'joborder/delete/'+ orderid;
  	}
}


function UserCodeScript(orderid,usercode)
{
	$('#uc_'+orderid).html('<input type="text" name="updated_user_code" id="updated_user_code_'+ orderid +'" value="'+ usercode +'" style="width:50px; text-align:center;" /> <a href="javascript:void(0);" onclick="javascript:SaveUserCode(\''+ orderid +'\')" style="color:#0033CC;">SAVE</a> <a href="javascript:CancelUpdateUC(\''+ orderid +'\',\''+ usercode +'\');"style="color:#0033CC;">CANCEL</a>');
}


function CancelUpdateUC(orderid,usercode)
{
	$('#uc_'+orderid).html(usercode +'<br />&laquo;&laquo;<a href="javascript:void(0);" onclick="javascript:UserCodeScript(\''+ orderid +'\',\''+ usercode +'\');" style="color:#0033CC;">UPDATE</a>&raquo;&raquo;');
}

function SaveUserCode(orderid)
{
	var newusercode = $('#updated_user_code_'+orderid).val();
	var base_url = $('#base_url').val();
	
	$.ajax({
		type : 'POST',
		url : base_url+'joborder/updateusercode',
		data: 'orderid='+ orderid +'&newusercode='+ newusercode,
		success : function(data)
		{
			$('#uc_'+orderid).html(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
			//alert(errorThrown);
		}
	});	
}

function VerifyMyQty(id)
{
	var ordered_qty = $('#ordered_qty_'+id).val();
	var exists_qty = $('#avail_qty'+id).val();
	
	if( Number(ordered_qty) > Number(exists_qty) )
	{
		alert('You can not order more than available stock. Available Stock '+exists_qty+' and you want '+ordered_qty);
		
		$('#ordered_qty_'+id).val('');
	}
}

function order_cancel()
{
	$('#order_status').val('cancel');
	
	var postData = $("#orderbook_view_form").serializeArray();
	
	$(".loading_div").show(); /*show loading element*/
	var page = 1; /*get page number from link*/
	$("#contents").load(base_url+'orderbook',{"page":page, "postData":postData}, function(){ /*get content from PHP page*/
		$(".loading_div").hide(); /*once done, hide loading element*/
		
		reloadScripts();
		
		if( $('#order_status').val() != '' )
			$('.found_row').show();
	});	
}

function poptastic(url)
{
	newwindow=window.open(url,'name',"location=1,status=1,scrollbars=1, width=880,height=680,resizable=1");
	if (window.focus) {newwindow.focus()}
}


function AddTrackingNum()
{
	var ordernum = $('#order').val();
	var counter = Number($('#counter').val()) + Number(1);
	
	$('#counter').val(counter);
	
	$('#etc_tracks').append('<tr id="'+counter+'"><td valign="top" align="left"><strong>Tracking Number for Order Number <font color="#FF0000">'+ ordernum +'</font> is :</strong></td><td align="left" colspan="3"><input type="text" class="inp-form" name="tracknum_etc[]" /></td></tr><tr id="'+counter+counter+'"><td valign="top" align="left"><strong>Freight Charges:</strong>$</th><td align="left"><input type="text" class="inp-form" name="freight_etc[]" /></td><td colspan="2"><a href="javascript:void(0);" onclick="RemoveTrackingNum('+counter+')" style="color:blue;">-Delete this Tracking Number and Freight Cost</a></td></tr>');
}

function RemoveTrackingNum(id)
{
	var counter = Number($('#counter').val()) - Number(1);
	
	$('#counter').val(counter);
	
	$('#'+id).remove();
	$('#'+id+id).remove();
}


function DoConfirm( id, controller, disp_name )
{
	if( confirm('Do you really want to delete this '+disp_name+'?') )
	{
		window.location.href = 	base_url+controller+"/delete/"+ id;
	}
}

function DoConfirmType( id, controller, disp_name )
{
	if( confirm('Do you really want to delete this '+disp_name+'?') )
	{
		window.location.href = 	base_url+controller+"/"+ id;
	}
}

function find_my_id(val)
{
	if( val == 'takeoff' )
		$('#my_id').html('<strong>TakeOff ID:</strong>');
	else if( val == 'quote' )
		$('#my_id').html('<strong>Quote ID:</strong>');
	else if( val == 'joborder' )
		$('#my_id').html('<strong>Job ID:</strong>');
	else if( val == 'orderbook' )
		$('#my_id').html('<strong>AWACP PO#:</strong>');
	else if( val == 'claims' )
		$('#my_id').html('<strong>Claim Code:</strong>');
}

function delete_file(doc_id, section)
{
	if( confirm('Are you sure? You want to delete this file?') )
	{
		if( doc_id != '' && section != '' )
		{
			$.ajax({
				type : 'POST',
				url : base_url+'deletefiles/delete',
				data: 'doc_id='+doc_id+'&section='+section,
				success : function(data)
				{	
					if( data == 1 )
					{
						alert('File deleted Succesfully.');
						$('#row_'+doc_id).remove();
					}
					else
						alert('File could not be deleted. Please try again.');
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) { 
					//alert(errorThrown);
				}
			});		
		}
	}
}

function setColor(flag, id)
{
	$.ajax({
		type : 'POST',
		url : base_url+'manufacture/setcolor',
		data: 'id='+id+'&flag='+flag,
		success : function(data)
		{	
			if( flag == 0 )
			{
				$('#set_'+id).attr('src',base_url+'resources/images/1.png');
				$('#set_'+id).attr('onClick','setColor(1,'+id+')');
				
				alert('Do not forget to enter message for this Product Type');
				
				$('#msg_'+id).focus();
			}
			else if( flag == 1 )
			{
				$('#set_'+id).attr('src',base_url+'resources/images/0.png');
				$('#set_'+id).attr('onClick','setColor(0,'+id+')');
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
				//alert(errorThrown);
		}
	});	
			
	
}

function SaveMsg(id, msg)
{
	$.ajax({
		type : 'POST',
		url : base_url+'manufacture/setmsg',
		data: 'id='+id+'&msg='+msg,
		success : function(data)
		{	
			if( data.trim() == 1 )
				alert('Message Saved!!');
			else
				alert('Message could not save. Try again.');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
				//alert(errorThrown);
		}
	});	

}

function GetClaim(ordernum)
{
	if( ordernum != '' )
	{
		$.ajax({
			type : 'POST',
			url : base_url+'truckerclaim/getClaim',
			data: 'ordernum='+ordernum,
			dataType: 'json',
			success : function(data)
			{	
				$('#rest-form').show();
				
				$('#orbf').val(data.orbf_number);
				$('#awpo').val(data.ordernumber);
				$('#contractor').val(data.cust_name);
				$('#track_num').val(data.tracking_num);
				$('#salesman').val(data.salesman);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) { 
				//alert(errorThrown);
			}
		});	
	}
}

function GetRecievers(salesman)
{
	if( salesman != '' )
	{
		$.ajax({
			type : 'POST',
			url : base_url+'marketing/recievers',
			data: 'salesman='+salesman,
			success : function(data)
			{	
				$('#rec_div').show();
				$('#rec_div').html(data);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) { 
				//alert(errorThrown);
			}
		});	
	}
	else
	{
		$('#rec_div').hide();
		$('#rec_div').html('');
	}
}

function editTax(county, taxrate, tid, act)
{
	$('#taxform').slideDown(300); 
	$('#add_tax_form').show(100);
	
	$('#county').val(county);
	$('#tax_rate').val(taxrate);
	$('#tid').val(tid);
	$('#action').val('edit');
}

function edTaxcancel()
{
	$('#taxform').slideUp(300); 
	$('#add_tax_form').hide(100);
	
	$('#county').val('');
	$('#tax_rate').val('');
	$('#tid').val('');
	$('#action').val('add');
}

function editInval(inventory, value, id, act)
{
	$('#invform').slideDown(300); 
	$('#add_inv_form').show(100);
	
	$('#inventory').val(inventory);
	$('#value').val(value);
	$('#id').val(id);
	$('#action').val('edit');
}

function edValcancel()
{
	$('#invform').slideUp(300); 
	$('#add_inv_form').hide(100);
	
	$('#inventory').val('');
	$('#value').val('');
	$('#id').val('');
	$('#action').val('add');
}

function editFactory(factory_code, name, id, act)
{
	$('#factform').slideDown(300); 
	$('#add_fact_form').show(100);
	
	$('#factory_code').val(factory_code);
	$('#name').val(name);
	$('#id').val(id);
	$('#action').val('edit');
}

function edFactcancel()
{
	$('#factform').slideUp(300); 
	$('#add_fact_form').hide(100);
	
	$('#factory_code').val('');
	$('#name').val('');
	$('#id').val('');
	$('#action').val('add');
}

function SiteSettings(current_form)
{
	$('#update_sitesettings').hide();
	$('#update_sitecolors').hide();
	$('#update_siteemails').hide();
	
	$('#'+current_form).show();
}

function log_dropdown()
{
	var postData = $("#logs_view_form").serializeArray();
	
	$(".loading_div").show(); /*show loading element*/
	var page = 1; /*get page number from link*/
	$("#contents").load(base_url+'logs',{"page":page, "postData":postData}, function(){ /*get content from PHP page*/
		$(".loading_div").hide(); /*once done, hide loading element*/
		
		reloadScripts();
		
	});	
}


function editProduct(name, id, act)
{
	$('#productform').slideDown(300); 
	$('#product_add_form').show(100);
	
	$('#name').val(name);
	$('#id').val(id);
	$('#action').val('edit');
}

function edProcancel()
{
	$('#productform').slideUp(300); 
	$('#product_add_form').hide(100);
	
	$('#name').val('');
	$('#id').val('');
	$('#action').val('add');
}

function formatPhone(id)
{
	var curchr = $('#'+id).val().length;
	
	var curval = $('#'+id).val();
	if (curchr == 3) {
		$("#"+id).val("(" + curval + ")" + "");
	} else if (curchr == 8) {
		$("#"+id).val(curval + "-");
	}	
}
