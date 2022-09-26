package com.p2psim;

import java.util.ArrayList;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Network<T extends Node> {
    private static int port_counter = 0;

    private ArrayList<T> nodes = new ArrayList<T>();

    public Network(){
        
    }

    private int get_free_port() {
        return port_counter++;
    }

    public T get_node(int port){
        for(T node : this.nodes){
            if(node.getPort() == port){
                return node;
            }
        }
        return null;
    }

    public T create_node(){
        int port = get_free_port();

        try {
            Type sooper = getClass().getGenericSuperclass();
            Type t = ((ParameterizedType)sooper).getActualTypeArguments()[ 0 ];
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) Class.forName( t.toString() );
            T node = (T) clazz.getDeclaredConstructor(Integer.class).newInstance(port);
            this.nodes.add(node);

            return node;
        }
        catch( Exception e ) {
            return null;
        }
    }
}
