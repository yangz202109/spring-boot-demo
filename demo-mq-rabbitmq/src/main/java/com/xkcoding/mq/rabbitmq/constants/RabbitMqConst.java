package com.xkcoding.mq.rabbitmq.constants;

public class RabbitMqConst {
    
    /*--------------------交换机---------------------------*/
    /**普通交换机*/
    public final static String NORMAL_EXCHANGE = "normal_exchange";
    /**死信交换机*/
    public final static String DEAD_EXCHANGE = "dead_exchange";
    /**发布确认交换机*/
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";
    /**备份交换机*/
    public static final String BACKUP_EXCHANGE = "backup_exchange";
    /**延迟交换机*/
    public static final String DELAYED_EXCHANGE = "delayed_exchange";


    /*--------------------队列---------------------------*/
    /**普通队列*/
    public final static String NORMAL_QUEUE1 = "normal_queue1";
    public final static String NORMAL_QUEUE2 = "normal_queue2";
    public final static String NORMAL_QUEUE3 = "normal_queue3";
    /**死信队列*/
    public final static String DEAD_QUEUE = "dead_queue";
    /**发布确认队列*/
    public static final String CONFIRM_QUEUE = "confirm_queue";
    /**备份队列*/
    public static final String BACKUP_QUEUE = "backup_queue";
    /**报警队列*/
    public static final String WARNING_QUEUE = "warning_queue";
    /**延迟队列*/
    public static final String DELAYED_QUEUE = "delayed_queue";


    /*--------------------路由key---------------------------*/
    /**普通路由key*/
    public final static String NORMAL_ROUTING_KEY1 = "normal_routing_key1";
    public final static String NORMAL_ROUTING_KEY2 = "normal_routing_key2";
    public final static String NORMAL_ROUTING_KEY3 = "normal_routing_key3";
    /**死信路由key*/
    public static final String DEAD_ROUTING_KEY = "dead_routing_key";
    /**发布确认路由key*/
    public static final String CONFIRM_ROUTING_KEY = "confirm_routing_key";
    /**延迟路由key*/
    public static final String DELAYED_ROUTING_KEY = "delayed_routing_key";

}
