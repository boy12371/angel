com.feinno.ha 高可用性集群管理
======================================================

Genesis: 服务启动入口  
------------------------------------------------------

# Genesis: Main
# ServiceSettings: 服务配置接口(local/ha)
# ServiceAgent: 服务启动相关
# WorkerAgent: 与HACenter建立链接
# 

服务运行方式
------------------------------------------------------
1. 自行启动, ha-center模式, (可能有监控外壳)
-> hamasterd local
2. 正式启动, worker连接ha-center, 同时连接ha-masterd
3. 测试启动, worker连接ha-center, 注册WorkerAgent
4. 开发启动, worker啥也不连, 
5. IDE启动，没有worker，配置采用数据库并执行本地优先策略

不启动Service的状态，Configurator设置成本地，没有Monitor，



Main Class
-------------------------------------------------------------------------
Genesis				main启动类
ServiceSettings		ha.xml-及其他方式的映射方式
ServiceOpts			帮助
WorkerAgent			与HACenter沟通
WorkerControllee	受控
