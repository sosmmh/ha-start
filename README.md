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