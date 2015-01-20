Ext.define('MO.view.TreePanel', {
			extend : 'Ext.tree.Panel',
			alias : 'widget.treePanel',
			title : '订阅列表',
			resizable : false,
			rootVisible : false,
			autoScroll : true,
			useArrows : true,
			autoHeight : true,
			collapsible : true,
			region : 'west',
			width : 150,
			initComponent : function() {
				Ext.Ajax.request({
							url : '/GetCategoryList',
							disableCaching : true,// 是否禁用缓存
							timeout : 10000,// 最大等待时间,超出则会触发超时
							success : function(resp, option) {// ajax请求发送成功时执行
								var body = Ext.decode(resp.responseText);
								appendTreeChild(body);
							},
							failure : function(data) {
								Ext.Msg.alert("failure",
										"GetCategoryList failure");
							}
						});
				function appendTreeChild(categoryJSON) {
					if (categoryJSON == null
							|| categoryJSON.categorys.length == 0) {
						return;
					}
					var treeAarry = Ext.ComponentQuery.query('treePanel');
					var tree = treeAarry[0];
					var root = tree.getRootNode();
					for (var i = 0; i < categoryJSON.categorys.length; i++) {
						var category = categoryJSON.categorys[i];
						var newNode = ({
							id : category.category.instance,
							text : category.category.name,
							leaf : false,
							url : ''
						});
						newNode.children = new Array();
						if (category.category.columns != null
								&& category.category.columns.length > 0) {
							for (var j = 0; j < category.category.columns.length; j++) {
								newNode.children[j] = {
									text : category.category.columns[j].counter.name,
									leaf : true
								};
							}
						}
						root.appendChild(newNode);
					}
				}
				
				this.tbar = [{
					text : 'ALL',
					handler : function(button) {
						
					}
				},{
					text : 'SUM',
					handler : function(button) {
						
					}
				}]
				
				this.callParent(arguments);
			}
		});