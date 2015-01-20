package com.test.feinno.ha.deployment;

import java.util.List;

import com.feinno.ha.deployment.HADeployment;
import com.feinno.ha.deployment.HADeploymentOne;
import com.feinno.ha.deployment.HAStatusEnum;
import com.feinno.ha.deployment.HATask;
import com.feinno.ha.deployment.HATaskResult;
import com.feinno.threading.Future;
import com.feinno.threading.FutureGroup;
import com.feinno.util.EventHandler;

public class HADeploymentTester {

	public void testGetStatus(HADeployment deployment) {
		// 循环调用GetStatus进行测试
		for (final HADeploymentOne deploymentOne : deployment.getDeploymentOnes()) {
			try {
				Future<Exception> future = deploymentOne.getStatus();
				future.addListener(new EventHandler<Exception>() {
					@Override
					public void run(Object sender, Exception e) {
						System.out.println(deploymentOne.getServer().getServerName() + "  "
								+ deploymentOne.getServer().getMasterCenterEp() + " result : "
								+ (e == null ? "sucess" : e.toString()));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void testStart(HADeployment deployment) {
		try {
			// 循环调用Start进行测试
			for (final HADeploymentOne deploymentOne : deployment.getDeploymentOnes()) {
				String statusStr = deploymentOne.getWorkerStatus().getWorkerStatus().toUpperCase();
				HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
				if (workerStatus != HAStatusEnum.MasterWorker.STANDBY) {
					continue;
				}
				Future<Exception> future = deploymentOne.start();
				future.addListener(new EventHandler<Exception>() {
					@Override
					public void run(Object sender, Exception e) {
						System.out.println(deploymentOne.getServer().getServerName() + "  "
								+ deploymentOne.getServer().getMasterCenterEp() + " result : "
								+ (e == null ? "sucess" : e.toString()));
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testStop(HADeployment deployment) {
		try {
			// 循环调用Start进行测试
			for (final HADeploymentOne deploymentOne : deployment.getDeploymentOnes()) {
				String statusStr = deploymentOne.getWorkerStatus().getWorkerStatus().toUpperCase();
				HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
				if (workerStatus != HAStatusEnum.MasterWorker.RUNNING) {
					continue;
				}
				Future<Exception> future = deploymentOne.stop();
				future.addListener(new EventHandler<Exception>() {
					@Override
					public void run(Object sender, Exception e) {
						System.out.println(deploymentOne.getServer().getServerName() + "  "
								+ deploymentOne.getServer().getMasterCenterEp() + " result : "
								+ (e == null ? "sucess" : e.toString()));
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testKill(HADeployment deployment) {
		try {
			// 循环调用Start进行测试
			for (final HADeploymentOne deploymentOne : deployment.getDeploymentOnes()) {
				try {
					Future<Exception> future = deploymentOne.kill();
					future.addListener(new EventHandler<Exception>() {
						@Override
						public void run(Object sender, Exception e) {
							System.out.println(deploymentOne.getServer().getServerName() + "  "
									+ deploymentOne.getServer().getMasterCenterEp() + " result : "
									+ (e == null ? "sucess" : e.toString()));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testKillShadow(HADeployment deployment) {
		try {
			// 循环调用Start进行测试
			for (final HADeploymentOne deploymentOne : deployment.getDeploymentOnes()) {
				try {
					Future<Exception> future = deploymentOne.killShadow();
					future.addListener(new EventHandler<Exception>() {
						@Override
						public void run(Object sender, Exception e) {
							System.out.println(deploymentOne.getServer().getServerName() + "  "
									+ deploymentOne.getServer().getMasterCenterEp() + " result : "
									+ (e == null ? "sucess" : e.toString()));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testHotSwap(HADeployment deployment) {
		try {
			// 循环调用Start进行测试
			for (final HADeploymentOne deploymentOne : deployment.getDeploymentOnes()) {
				String statusStr = deploymentOne.getWorkerStatus().getWorkerStatus().toUpperCase();
				HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
				if (workerStatus != HAStatusEnum.MasterWorker.RUNNING) {
					continue;
				}
				Future<Exception> future = deploymentOne.hotSwap();
				future.addListener(new EventHandler<Exception>() {
					@Override
					public void run(Object sender, Exception e) {
						System.out.println(deploymentOne.getServer().getServerName() + "  "
								+ deploymentOne.getServer().getMasterCenterEp() + " result : "
								+ (e == null ? "sucess" : e.toString()));
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testStartAll(HADeployment deployment) {
		Future<HATaskResult> future = WaitAll(deployment.startAll());
		future.addListener(new EventHandler<HATaskResult>() {
			@Override
			public void run(Object sender, HATaskResult e) {
				System.out.println(e.isSucess() ? "sucess" : "failed : " + e.getExceptions());
			}
		});
	}

	public void testStopAll(HADeployment deployment) {
		Future<HATaskResult> future = WaitAll(deployment.stopAll());
		future.addListener(new EventHandler<HATaskResult>() {
			@Override
			public void run(Object sender, HATaskResult e) {
				System.out.println(e.isSucess() ? "sucess" : "failed : " + e.getExceptions());
			}
		});
	}

	public void testKillAll(HADeployment deployment) {
		Future<HATaskResult> future = WaitAll(deployment.killAll());
		future.addListener(new EventHandler<HATaskResult>() {
			@Override
			public void run(Object sender, HATaskResult e) {
				System.out.println(e.isSucess() ? "sucess" : "failed : " + e.getExceptions());
			}
		});
	}

	public void testUpdateAll(HADeployment deployment) {
		Future<HATaskResult> future = WaitAll(deployment.updateAll());
		future.addListener(new EventHandler<HATaskResult>() {
			@Override
			public void run(Object sender, HATaskResult e) {
				System.out.println(e.isSucess() ? "sucess" : "failed : " + e.getExceptions());
			}
		});
	}

	public void testDelayStopAll(HADeployment deployment) {
		Future<HATaskResult> future = WaitAll(deployment.delayStopAll());
		future.addListener(new EventHandler<HATaskResult>() {
			@Override
			public void run(Object sender, HATaskResult e) {
				System.out.println(e.isSucess() ? "sucess" : "failed : " + e.getExceptions());
			}
		});
	}

	public static Future<HATaskResult> WaitAll(List<HATask> futures) {
		final Future<HATaskResult> ret = new Future<HATaskResult>();
		FutureGroup<HATask> futureGroup = new FutureGroup<HATask>(futures);
		futureGroup.addListener(new EventHandler<List<HATask>>() {
			@Override
			public void run(Object sender, List<HATask> e) {
				HATaskResult taskResult = new HATaskResult("", "startAll");
				taskResult.setSucess(true);
				for (HATask future : e) {
					taskResult.setServiceName(future.getServiceName());
					if (future.getValue() != null) {
						taskResult.setSucess(false);
						taskResult.addException(future.getValue());
					}
				}
				ret.complete(taskResult);
			}
		});
		return ret;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HADeployment deployment = new HADeployment("test", "SiteC");
		System.out.println(deployment);
		new HADeploymentTester().testGetStatus(deployment);
		// new HADeploymentTester().testStart(deployment);
		// new HADeploymentTester().testStop(deployment);
		// new HADeploymentTester().testKill(deployment);
		// new HADeploymentTester().testStart(deployment);
		// new HADeploymentTester().testHotSwap(deployment);
		// new HADeploymentTester().testKillShadow(deployment);

		// new HADeploymentTester().testDelayStopAll(deployment);
		// new HADeploymentTester().testStopAll(deployment);
		// new HADeploymentTester().testKillAll(deployment);
		// new HADeploymentTester().testStartAll(deployment);

	}

}
