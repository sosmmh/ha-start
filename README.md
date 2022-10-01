![image](https://user-images.githubusercontent.com/30424736/169301672-874a8c10-91df-457c-9ccd-6d07cfadee2e.png)

# 初始化本地消息表
ha.sql


# 版本1.0
#### 1. 依赖jar包
        <dependency>
            <groupId>com.sosmmh.demo</groupId>
            <artifactId>ha-start</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

#### 2. 实现可靠发送组件
###### 2.1 如果是使用Kafka发送消息，实现ReliableProducer，
    @Slf4j
    public class ReliableKafkaProducer extends AbstractReliableProducer {
        
        private KafkaTemplate kafkaTemplate;
        
        @Override
        public boolean actualSend(String messageId, String topic, String message) {
            log.info("msgId={}|事务成功提交|simpleLog|topic={}|message={}", messageId, topic, message);
            kafkaTemplate.send(topic, message);
            // 根据发送成功与否，返回true或者false
            return true;
        }
    }

#### 2. 初始化bean
    @Bean
    public ReliableStore reliableStore(JdbcTemplate jdbcTemplate) {
        ReliableStore reliableStore = new ReliableMysql(jdbcTemplate);
        return reliableStore;
    }

    @Bean
    public ReliableProducer reliableProducer(ReliableStore reliableStore) {
        ReliableKafkaProducer kafkaProducer = new ReliableKafkaProducer();
        kafkaProducer.init(reliableStore);
        return kafkaProducer;
    }

#### 3. 使用
@Service
public class UserServiceImpl {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReliableProducer reliableProducer;

    @Transactional(rollbackFor = Exception.class)
    public void add() {
        User user = new User();
        user.setName("xiahan.li");
        user.setAge(28);
        user.setEmail("670759953@qq.com");
        userMapper.insert(user);
        reliableProducer.send("123", JSONObject.toJSONString(user));
    }
}


# 版本2.0
## 1. 引入starter包即可实现事务消息
        <dependency>
            <groupId>com.sosmmh.demo</groupId>
            <artifactId>ha-starter-simplelog</artifactId>
            <version>2.0-SNAPSHOT</version>
        </dependency>
## 2. 若使用kafka的starter包，则引入
###### 业务项目需自行配置好kafka相关的地址
        <dependency>
            <groupId>com.sosmmh.demo</groupId>
            <artifactId>ha-starter-kafka</artifactId>
            <version>2.0-SNAPSHOT</version>
        </dependency>

# 版本2.1
## 1. 基于版本2.0的基础上增加配置项

    hamq:
        ha:
            # 表示使用什么mq
            type: simple
            # 定时任务重试n次
            max-retries: 3
            # 重试间隔
            retry-period: 30000
            # 发送超时时间
            timeout: 5000`
## 2. 改变发送消息方式
    @Transactional(rollbackFor = Exception.class)
    public void add() {
        User user = new User();
        user.setName("xiahan.li");
        user.setAge(28);
        user.setEmail("670759953@qq.com");
        userMapper.insert(user);
        
        # 若使用Kafka，则在sendArgs添加配置信息，比如topic、key、partition
        KafkaArgs sendArgs = new KafkaArgs();
        sendArgs.setTopic("ha-topic");
        HaMessage haMessage = HaMessage.builder()
                .refId("123")
                .message(JSONObject.toJSONString(user))
                .sendArgs(sendArgs)
                .build();

        reliableProducer.send(haMessage);
    }
