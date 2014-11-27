#jssdb
#####java connection pool for ssdb
######支持多主多从，宕机自动连接下一个可用服务器，服务器恢复后自动进入待连接队列


###属性文件中配置：<br />
	MASTER_HOST_PORT_TIME=ip1:port1:timeout1;ip2:port2:timeout2
	SLAVER_HOST_PORT_TIME=ip3:port3:timeout3;ip4:port4:timeout4

###在spring中集成jssdbClient，代码如下：

	<bean id="jssdbPoolConfig" class="com.wj.jssdb.pool.JssdbPoolConfig" >
       	<!-- 最大等待数；默认8 -->
		<property name="maxIdle" value="30" />
		<!-- 连接池最小等待数；默认0 -->
		<property name="minIdle" value="10" />
		<!-- 最大活动数；默认8 -->
		<property name="maxActive" value="60" />
		<!-- 等待时间，毫秒，最长等待时间，当池中的对象被取完没有返回到池时，等待时间，当有返回的对象时就取出，到时间还没就抛错；默认-1表示永久等待 -->
		<property name="maxWait" value="-1" />
		<!-- 当调用borrow，pool维护的对象超过maxActive时，通过配置将会出现如下动作；默认1；GenericObjectPool.WHEN_EXHAUSTED_* -->
		<property name="whenExhaustedAction" value="1" />
		<!-- 设定在进行后台对象清理时，每次检查几个对象。如果这个值不是正数；默认3 -->
		<!-- 则每次检查的对象数是检查时池内对象的总数乘以这个值的负倒数再向上取整的结果―― -->
		<!-- 也就是说，如果这个值是-2（-3、-4、-5……）的话，那么每次大约检查当时池内对象总数的1/2（1/3、1/4、1/5……）左右 -->
		<property name="numTestsPerEvictionRun" value="3" />
		<!-- 设定在借出对象时是否进行有效性检查；默认false -->
		<property name="testOnBorrow" value="false" />
		<!-- 设定在还回对象时是否进行有效性检查；默认false -->
		<property name="testOnReturn" value="false" />
		
		<!-- 定义清理扫描的时间间隔。配置为负值，不需要执行清理线程，默认为-1 -->
		<property name="timeBetweenEvictionRunsMillis" value="-1" />
		<!-- 设定在进行后台对象清理时，视休眠时间超过了多少毫秒的对象为过期。默认1800000毫秒，30分钟 -->
		<!-- 过期的对象将被回收。如果这个值不是正数，那么对休眠时间没有特别的约束。 -->
		<property name="minEvictableIdleTimeMillis" value="1800000" />
		<!-- 在清理时，可以预留一定数量的idle 对象，是minEvictableIdleTimeMillis的一个附加条件， -->
		<!-- 如果为负值，表示如果清理，则所有idle对象都被清理；默认：-1 -->
		<property name="softMinEvictableIdleTimeMillis" value="10" />
		<!-- 则设定在进行后台对象清理时，是否还对没有过期的池内对象进行有效性检查。不能通过有效性检查的对象也将被回收；默认false -->
		<property name="testWhileIdle" value="false" />
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
