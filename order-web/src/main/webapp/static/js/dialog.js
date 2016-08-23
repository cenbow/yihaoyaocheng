/***
 * *dialog 页面弹框组件
 * *
 * *****/    
//弹框
function Dialog(options) {
	this.title = null;
	this.content = null;
	this.ok = null;
	this.cancel = null;
	this.canMaskClose = true;
	this.className = '';
	this.type = 'dialog';
	this.container = document.createElement('div');
	this.footer = document.createElement('div');
	this.mask = '<div class="dialog-mask-bg"></div>';
	this.config(options).open();
	return this;
}
Dialog.prototype = {
	constructor: Dialog,
	config: function (options) {
		if (typeof options === 'object') {			
			for (var i in options) {
				this[i] = options[i]
			}
		}
		return this;
	},
	handleEvent: function (e) {
		var target = e.target;
		var classList = target.classList;

        //点击遮罩层,不做任何事情,直接摧毁弹框
        if (classList.contains('dialog-mask-bg')) {
        	this.canMaskClose ? this.close() : null; 			
        	return
        }
        //点击确定按钮
        if (classList.contains('dialog-ok-btn')) {
        	this.confirm()
        	return
        }
        //点击取消按钮
        if (classList.contains('dialog-cancel-btn')) {
        	this.close()
        }

        //点击关闭的图标
        if (classList.contains('dialog-close')) {
        	this.destory()
        }

    },
    open: function() {
    	this.container.className = "widget-dialog "+this.className;
    	this.title = this.title ? '<div class="w-d-title"><h3>' + this.title + '</h3><a href="javascript:;" class="common-icon dialog-close"></a></div>' :'';
    	this.content = this.content ? '<div class="w-d-content">' + this.content + '</div>' : '';
    	this.ok = this.ok ? '<a href="javascript:;" class="dialog-ok-btn">' + this.ok + '</a>' : ''; 
    	this.cancel = this.cancel ? '<a href="javascript:;" class="dialog-cancel-btn newstyle-btn">'+ this.cancel +'</a>' : ''; 
    	this.footerBtn = this.ok + this.cancel;
    	this.footer = '<div class="w-d-foot">'+ this.footerBtn +'</div>';
    	this.main = this.title + this.content + this.footer;
    	this.container.innerHTML = '<div class="w-d-main">' + this.main + '</div>' + this.mask;
    	var body = document.body;
    	var self = this;
    	$('.w-d-main').append(this.footer);
    	body.appendChild(this.container);

        this.afterOpen();

        this.container.addEventListener('click',this)

    },
    afterOpen: function () {
    },
    confirm: function () {
    	this.afterOk();
        this.destory();
    },
    close: function () {
    	this.afterClose();
    	this.destory();

    },
    afterOk: function () {
    },
    afterClose: function () {
    },
    destory: function () {
    	var body = document.body;
    	body.removeChild(this.container);
    }
};

