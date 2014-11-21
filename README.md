jssdb
=====
java connection pool for ssdb
-------

在spring中继承jssdbClient，代码如下：

	<bean id="jssdbPoolConfig" class="com.wj.jssdb.pool.JssdbPoolConfig" >
       	<property name="testOnBorrow" value="true"/> <!-- set for re-connect -->
		<property name="maxActive" value="30" /> 
		<property name="maxIdle" value="10" /> 
		<property name="maxWait" value="1000" /> 
    </bean>
	<bean id="masterJssdbPool" class="com.wj.jssdb.pool.JssdbPool">
	    <constructor-arg index="0" ref="jssdbPoolConfig"/>
		<constructor-arg index="1" type="String" value="${MASTER_HOST_PORT_TIME}" />
	</bean>
	<bean id="slaverJssdbPool" class="com.wj.jssdb.pool.JssdbPool">
	    <constructor-arg index="0" ref="jssdbPoolConfig"/>
		<constructor-arg index="1" type="String" value="${SLAVER_HOST_PORT_TIME}" />
	</bean>
	<!-- write/read client -->
	<bean id="jssdbClient" class="com.wj.jssdb.pool.JssdbClient">
		<property name="masterJssdbPool" ref="masterJssdbPool" />
		<property name="slaverJssdbPool" ref="slaverJssdbPool" />
	</bean>
