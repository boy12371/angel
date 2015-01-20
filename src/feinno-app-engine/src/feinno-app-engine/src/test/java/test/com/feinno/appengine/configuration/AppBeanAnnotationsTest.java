package test.com.feinno.appengine.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.AppBeanAnnotationsLoader;
import com.feinno.appengine.configuration.FAEAppAnnotationDecoder;
import com.feinno.appengine.context.NullContext;
import com.feinno.appengine.rpc.RemoteAppBean;
import com.feinno.appengine.rpc.RemoteAppTx;
import com.feinno.util.EnumInteger;
import com.google.gson.Gson;

public class AppBeanAnnotationsTest {
    
    public static void main(String[] args)
    {
        try{
            Gson gson = new Gson();
            AppBeanAnnotations appBeanAnno = null;
            System.out.println("==============================" + BasicAnnoAppBean.class.getCanonicalName() + "==============================");
            appBeanAnno = AppBeanAnnotationsLoader.getAppBeanAnnotaions(BasicAnnoAppBean.class);
            String str = gson.toJson(appBeanAnno);
            Object obj = gson.fromJson(str , AppBeanAnnotations.class);
            String str2 = gson.toJson(obj);
            Object obj2 = FAEAppAnnotationDecoder.decode(str);
            String str3 = gson.toJson(obj2);
            System.out.println(str);
            System.out.println(str.equals(str2)?"==":"<>");
            System.out.println(str2);
            System.out.println(str2.equals(str3)?"==":"<>");
            System.out.println(str3);

            System.out.println("==============================" + AnnoArrayAppBean.class.getCanonicalName() + "==============================");
            appBeanAnno = AppBeanAnnotationsLoader.getAppBeanAnnotaions(AnnoArrayAppBean.class);
            str = gson.toJson(appBeanAnno);
            obj = gson.fromJson(str , AppBeanAnnotations.class);
            str2 = gson.toJson(obj);
            obj2 = FAEAppAnnotationDecoder.decode(str);
            str3 = gson.toJson(obj2);
            System.out.println(str);
            System.out.println(str.equals(str2)?"==":"<>");
            System.out.println(str2);
            System.out.println(str2.equals(str3)?"==":"<>");
            System.out.println(str3);

            System.out.println("==============================" + Anno2ArrayAppBean.class.getCanonicalName() + "==============================");
            appBeanAnno = AppBeanAnnotationsLoader.getAppBeanAnnotaions(Anno2ArrayAppBean.class);
            str = gson.toJson(appBeanAnno);
            obj = gson.fromJson(str , AppBeanAnnotations.class);
            str2 = gson.toJson(obj);
            obj2 = FAEAppAnnotationDecoder.decode(str);
            str3 = gson.toJson(obj2);
            System.out.println(str);
            System.out.println(str.equals(str2)?"==":"<>");
            System.out.println(str2);
            System.out.println(str2.equals(str3)?"==":"<>");
            System.out.println(str3);

        } catch(Throwable ex) {
            ex.printStackTrace();
        }
    }

    
    
    public static enum SmsMoType implements EnumInteger
    {
        RAW(0),
        SID(1),
        MOBILE(2),
        SUBCODE(3),
        SUBCODE_SID(4),
        SUBCODE_MOBILE(5),
        SUBCODE_X(6);

        private SmsMoType(int v)
        {
            this.value = v;
        }

        private int value;

        @Override
        public int intValue()
        {
            return value;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public static @interface SmsCommands
    {
        SmsCommand[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public static @interface SmsCommand2s
    {
        SmsCommands[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public static @interface SmsCommand 
    {
        SmsMoType type();
        String subcode() default "";
        String text() default "";
    }


    @AppName(category = "core", name = "SampleSmsAppBean")
    @SmsCommands({
            @SmsCommand(type=SmsMoType.SUBCODE_MOBILE, subcode="026", text="YQ"),
            @SmsCommand(type=SmsMoType.RAW, text="YQ")
    })
    public static class AnnoArrayAppBean extends RemoteAppBean<Integer, String, NullContext>
    {

        /* (non-Javadoc)
         * @see com.feinno.appengine.rpc.RemoteAppBean#process(com.feinno.appengine.rpc.RemoteAppTx)
         */
        @Override
        public void process(RemoteAppTx<Integer, String, NullContext> tx) throws Exception {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see com.feinno.appengine.AppBean#setup()
         */
        @Override
        public void setup() throws Exception {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see com.feinno.appengine.AppBean#load()
         */
        @Override
        public void load() throws Exception {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see com.feinno.appengine.AppBean#unload()
         */
        @Override
        public void unload() throws Exception {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    @AppName(category = "core", name = "SampleSmsAppBean")
    @SmsCommand2s({
            @SmsCommands({
                @SmsCommand(type=SmsMoType.SUBCODE_MOBILE, subcode="026-1", text="YQ-1"),
                @SmsCommand(type=SmsMoType.RAW, text="YQ-1")
            }),
            @SmsCommands({
                @SmsCommand(type=SmsMoType.SUBCODE_MOBILE, subcode="026-2", text="YQ-2"),
                @SmsCommand(type=SmsMoType.RAW, text="YQ-2")
            })

    })
    public static class Anno2ArrayAppBean extends RemoteAppBean<Integer, String, NullContext>
    {

        /* (non-Javadoc)
         * @see com.feinno.appengine.rpc.RemoteAppBean#process(com.feinno.appengine.rpc.RemoteAppTx)
         */
        @Override
        public void process(RemoteAppTx<Integer, String, NullContext> tx) throws Exception {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see com.feinno.appengine.AppBean#setup()
         */
        @Override
        public void setup() throws Exception {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see com.feinno.appengine.AppBean#load()
         */
        @Override
        public void load() throws Exception {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see com.feinno.appengine.AppBean#unload()
         */
        @Override
        public void unload() throws Exception {
            // TODO Auto-generated method stub
            
        }
        
    }

    @AppName(category = "core", name = "SampleSmsAppBean")
    @SmsCommand(type=SmsMoType.SUBCODE_MOBILE, subcode="026", text="YQ")
    public static class BasicAnnoAppBean extends RemoteAppBean<Integer, String, NullContext>
    {

        /* (non-Javadoc)
         * @see com.feinno.appengine.rpc.RemoteAppBean#process(com.feinno.appengine.rpc.RemoteAppTx)
         */
        @Override
        public void process(RemoteAppTx<Integer, String, NullContext> tx) throws Exception {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see com.feinno.appengine.AppBean#setup()
         */
        @Override
        public void setup() throws Exception {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see com.feinno.appengine.AppBean#load()
         */
        @Override
        public void load() throws Exception {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see com.feinno.appengine.AppBean#unload()
         */
        @Override
        public void unload() throws Exception {
            // TODO Auto-generated method stub
            
        }
        
    }
}
