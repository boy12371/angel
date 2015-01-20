//package test.com.feinno.serialization.protobuf;
//
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import test.com.feinno.serialization.protobuf.TestInnerClassProto.TestProtoEntity.InnerInnerProtoEntity;
//
//import com.feinno.serialization.Serializer;
//import com.feinno.serialization.protobuf.ProtoEntity;
//import com.feinno.serialization.protobuf.ProtoManager;
//import com.feinno.serialization.protobuf.ProtoMember;
//
//
//public class TestInnerClassProto {
//	public static void main(String[] args) throws Exception {
//		ProtoManager.setDebug(true);
//		new TestInnerClassProto().testInnerClass();
//		ProtoManager.setDebug(false);
//	}
//
//	@Test
//	public void testInnerClass() {
//		try {
//
//			byte[] buffer = Serializer.encode(newTestProtoEntity());
//			System.out.println(Serializer.decode(TestProtoEntity.class, buffer).getName());
//			System.out.println(Serializer.decode(TestProtoEntity.class, buffer).getInnerProtoEntity().getName());
//			System.out.println(Serializer.decode(TestProtoEntity.class, buffer).getInnerProtoEntity()
//					.getInnerProtoEntity().getName());
//			System.out.println(Serializer.decode(TestProtoEntity.class, buffer).getInnerInnerProtoEntity().getName());
//
//		} catch (Exception e) {
//			Assert.assertTrue(false);
//		}
//
//	}
//
//	/**
//	 * 这是一个反面的例子，用它来证明无法将匿名内部类序列化，因为无法生成它的序列化辅助类(无法生成它的辅助类的原因是外部无法将这个匿名内部类实例化，
//	 * 匿名内部类会默认以$1、$2的名字出现,1或2的纯数字无法作为类名)
//	 */
//	@Test
//	public void testInnerNoNameClass() {
//		try {
//			Serializer.encode(new ProtoEntity() {
//
//				@ProtoMember(1)
//				private String name;
//
//				@SuppressWarnings("unused")
//				public final String getName() {
//					return name;
//				}
//
//				public final ProtoEntity setName(String name) {
//					this.name = name;
//					return this;
//				}
//			}.setName("huhuhu~~~"));
//		} catch (Exception e) {
//			Assert.assertTrue(true);
//			return;
//		}
//		Assert.assertTrue(false);
//	}
//
//	public static TestProtoEntity newTestProtoEntity() {
//		TestProtoEntity testProtoEntity = new TestProtoEntity();
//		InnerProtoEntity innerProtoEntity1 = new InnerProtoEntity();
//		InnerProtoEntity innerProtoEntity2 = new InnerProtoEntity();
//		InnerInnerProtoEntity innerInnerProtoEntity = new InnerInnerProtoEntity();
//		testProtoEntity.setName("LV");
//		innerProtoEntity1.setName("Ming");
//		innerProtoEntity2.setName("wei");
//		innerInnerProtoEntity.setName("innerInnerProtoEntity");
//		innerProtoEntity1.setInnerProtoEntity(innerProtoEntity2);
//		testProtoEntity.setInnerProtoEntity(innerProtoEntity1);
//		testProtoEntity.setInnerInnerProtoEntity(innerInnerProtoEntity);
//		return testProtoEntity;
//	}
//
//	public static class TestProtoEntity extends ProtoEntity {
//
//		@ProtoMember(1)
//		private String name;
//
//		@ProtoMember(2)
//		private InnerProtoEntity innerProtoEntity;
//
//		@ProtoMember(3)
//		private InnerInnerProtoEntity innerInnerProtoEntity;
//		
//		@ProtoMember(4)
//		private List<InnerProtoEntity> innerProtoEntityList;
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public final InnerProtoEntity getInnerProtoEntity() {
//			return innerProtoEntity;
//		}
//
//		public final void setInnerProtoEntity(InnerProtoEntity innerProtoEntity) {
//			this.innerProtoEntity = innerProtoEntity;
//		}
//
//		public InnerInnerProtoEntity getInnerInnerProtoEntity() {
//			return innerInnerProtoEntity;
//		}
//
//		public void setInnerInnerProtoEntity(InnerInnerProtoEntity innerInnerProtoEntity) {
//			this.innerInnerProtoEntity = innerInnerProtoEntity;
//		}
//		
//		public List<InnerProtoEntity> getInnerProtoEntityList() {
//			return innerProtoEntityList;
//		}
//
//		public void setInnerProtoEntityList(List<InnerProtoEntity> innerProtoEntityList) {
//			this.innerProtoEntityList = innerProtoEntityList;
//		}
//
//
//
//		public static class InnerInnerProtoEntity extends ProtoEntity {
//
//			@ProtoMember(1)
//			private String name;
//
//			public final String getName() {
//				return name;
//			}
//
//			public final void setName(String name) {
//				this.name = name;
//			}
//
//		}
//
//	}
//
//	public static class InnerProtoEntity extends ProtoEntity {
//
//		@ProtoMember(1)
//		private String name;
//
//		@ProtoMember(2)
//		private InnerProtoEntity innerProtoEntity;
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public InnerProtoEntity getInnerProtoEntity() {
//			return innerProtoEntity;
//		}
//
//		public void setInnerProtoEntity(InnerProtoEntity innerProtoEntity) {
//			this.innerProtoEntity = innerProtoEntity;
//		}
//
//	}
//
//}
