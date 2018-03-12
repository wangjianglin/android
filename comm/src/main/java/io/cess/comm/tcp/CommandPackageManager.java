package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.Command;

/**
 * @author lin
 * @date 1/28/16.
 */
public class CommandPackageManager {

    private static PackageManagerImpl<Integer,CommandRequestTcpPackage,CommandResponseTcpPackage> impl = new PackageManagerImpl<Integer,CommandRequestTcpPackage,CommandResponseTcpPackage>(new io.cess.util.Function<Integer,Class<?>>(){

        @Override
        public Integer function(Class<?> cls) {
            return getCommand(cls);
        }

    });

    private static int getCommand(Class<?> cls){
        if(cls == null
                || cls == CommandRequestTcpPackage.class
                || cls == Object.class ){
            return 0;
        }

        Command command = cls.getAnnotation(Command.class);

        if(command != null){
            return command.value();
        }

        return getCommand(cls.getSuperclass());
    }

    public static void regist(Class<? extends CommandRequestTcpPackage> cls){
        impl.regist((Class<CommandRequestTcpPackage>) cls);
    }

    public static int request(Class<? extends CommandResponseTcpPackage> cls){
        Integer command = impl.request(cls);
        if(command == null){
            return 0;
        }
        return command;
    }

    public static CommandRequestTcpPackage newRequestInstance(int key){

        return impl.newRequestInstance(key);
    }

    public static CommandResponseTcpPackage newResponseInstance(int key){

        return impl.newResponseInstance(key);
    }
}
