var loadingTimer,
    loadingTimeout = 500;

/**
 * On document is ready.
 */
$(document).ready(function() {
	
	/** browser window scroll (in pixels) after which the "back to top" link is shown */
	var offset = 300,
	/** browser window scroll (in pixels) after which the "back to top" link opacity is reduced */
	offset_opacity = 1200,
	/** duration of the top scrolling animation (in ms) */
	scroll_top_duration = 500,
	/** grab the "back to top" link */
	$back_to_top = $('.cd-top');

	/**
	 * Hide or show the "back to top" link
	 */ 
	$(window).scroll(function() {
		if ($(this).scrollTop() > offset) {
			$back_to_top.addClass('cd-is-visible');
			//alert($back_to_top.css('bottom'));
			if ($(this).scrollTop() + $(this).height() == $(document).height()) {
				$back_to_top.addClass('cd-top-end');
			} else {
				$back_to_top.removeClass('cd-top-end');
			}
		} else {
			$back_to_top.removeClass('cd-is-visible cd-fade-out');
		}
		if($(this).scrollTop() > offset_opacity) { 
			$back_to_top.addClass('cd-fade-out');
		}
	});

	/**
	 * Click event to smooth scroll to top
	 */ 
	$back_to_top.on('click', function(event) {
		event.preventDefault();
		$('body,html').animate({ scrollTop: 0}, scroll_top_duration);
	});
});

/**
  * jQuery UI - v1.11.0
  * Simply rewrites the goToToday method and adds two new lines:
  * 	- this._setDateDatepicker(target, date);
  *    	- this._selectDate(id, this._getDateDatepicker(target));
  */
jQuery.datepicker._gotoToday = function(id) {
	var date,
	target = jQuery(id),
	inst = this._getInst(target[0]);
	if (this._get(inst, "gotoCurrent") && inst.currentDay) {
		inst.selectedDay = inst.currentDay;
		inst.drawMonth = inst.selectedMonth = inst.currentMonth;
		inst.drawYear = inst.selectedYear = inst.currentYear;
	} else {
		date = new Date();
		inst.selectedDay = date.getDate();
		inst.drawMonth = inst.selectedMonth = date.getMonth();
		inst.drawYear = inst.selectedYear = date.getFullYear();
        this._setDateDatepicker(target, date);
        this._selectDate(id, this._getDateDatepicker(target));
	}
	this._notifyChange(inst);
	this._adjustDate(target);
}

/**
 * Override default behaviors to prevent calendar focus on dialog opening.
 * http://forum.primefaces.org/viewtopic.php?f=3&t=29050
 * 
 * Original code is:
 * 
 * PrimeFaces.widget.Dialog.prototype.applyFocus = function() {
 *     this.jq.find(':not(:submit):not(:button):input:visible:enabled:first').focus();
 * }
 */
PrimeFaces.widget.Dialog.prototype.applyFocus = function() {
	var firstInput = this.jq.find(':not(:submit):not(:button):input:visible:enabled:first');
	if (!firstInput.hasClass('hasDatepicker')) {
		firstInput.focus();
	}
}

/**
 * Override default behaviors to prevent calendar focus on overlay panel opening.
 * http://forum.primefaces.org/viewtopic.php?f=3&t=29050
 * 
 * Original code is:
 * 
 * PrimeFaces.widget.OverlayPanel.prototype.applyFocus = function() {
 *     this.jq.find(':not(:submit):not(:button):input:visible:enabled:first').focus();
 * }
 */
PrimeFaces.widget.OverlayPanel.prototype.applyFocus = function() {
	var firstInput = this.jq.find(':not(:submit):not(:button):input:visible:enabled:first');
	if (!firstInput.hasClass('hasDatepicker')) {
		firstInput.focus();
	}
}

function showMessageInDialogWithOk(msg) {
    if(!this.messageDialog) {
        $('<div id="primefacesmessagedlg" class="ui-message-dialog ui-dialog ui-widget ui-widget-content ui-corner-all ui-shadow ui-hidden-container"/>')
                    .append('<div class="ui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top"><span class="ui-dialog-title"></span>' +
                    '<a class="ui-dialog-titlebar-icon ui-dialog-titlebar-close ui-corner-all" href="#" role="button"><span class="ui-icon ui-icon-closethick"></span></a></div>' + 
                    '<div class="ui-dialog-content ui-widget-content" style="height: auto;"></div>' +
                    '<div class="ui-dialog-buttonpane ui-dialog-footer ui-widget-content ui-helper-clearfix">' +
                        '<div class="center">' +
                            '<button id="j_idt76" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left ui-confirmdialog-no application primary icon" ' + 
                                    'onclick="PF(\'primefacesmessagedialog\').hide();return false;" style="width:80px;" type="submit" role="button" aria-disabled="false">' +
                                    '<span class="ui-button-icon-left ui-icon ui-c fa fa-check"></span><span class="ui-button-text ui-c">Ok</span>' + 
                            '</button>' +
                        '</div>' +
                    '</div>').appendTo(document.body);

        PrimeFaces.cw('Dialog', 'primefacesmessagedialog', {
            id: 'primefacesmessagedlg', 
            modal:true,
            draggable: false,
            resizable: false,
            showEffect: 'fade',
            hideEffect: 'fade'
        });
        this.messageDialog = PF('primefacesmessagedialog');
        this.messageDialog.titleContainer = this.messageDialog.titlebar.children('span.ui-dialog-title');
    }

    this.messageDialog.titleContainer.text(msg.summary);
    this.messageDialog.content.html('').append('<span class="ui-dialog-message ui-messages-' + msg.severity.split(' ')[0].toLowerCase() + '-icon" />').append(msg.detail);
    this.messageDialog.show();
}

/**
 * Show busy overlay function
 * http://stackoverflow.com/questions/1964839/jquery-please-wait-loading-animation
 * http://jsfiddle.net/VpDUG/4952/
 */
function showWaitMe() {
	jQuery('body').addClass('loading');
}

/**
 * Hide busy overlay function
 * http://stackoverflow.com/questions/1964839/jquery-please-wait-loading-animation
 * http://jsfiddle.net/VpDUG/4952/
 */
function hideWaitMe() {
	jQuery('body').removeClass('loading');
}

function disableBrowserScroll() {
	jQuery('body').addClass('modal-open');
}

function enableBrowserScroll() {
	jQuery('body').removeClass('modal-open');
}

function onModalShow() {
	disableBrowserScroll();
}

function onModalHide() {
	enableBrowserScroll();
}

function isUndefined(obj) {
	return typeof obj == 'undefined' ? true : false;
}

function showWaitMeDelayed() {
	clearTimeout(loadingTimer);
	loadingTimer = setTimeout("showWaitMe()", loadingTimeout);				
}

function hideWaitMeDelayed() {
	clearTimeout(loadingTimer);
	hideWaitMe();
}

// ----------------------------------------------------------------------------
// -- Project specific code from here
// ----------------------------------------------------------------------------

function callAuditTrail(projectId, fileId) {
	var encodedProjectId = encodeURIComponent(projectId);
	var encodedFileId = encodeURIComponent(fileId);
	window.open('auditTrail?projectId=' + encodedProjectId + "&fileId=" + encodedFileId, '_blank');
}
