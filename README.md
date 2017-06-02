## How To Use

在Rancher"API-KEYS"下创建Account API Keys(账号API KEY),修改RancherConfig类中的信息为对应的KEY信息

```
public class RancherConfig {

    public static final String ENDPOINT = "<YOUR ENDPOINT>";
    public static final String ACCESSKEY = "<ACCESS KEY>";
    public static final String SECRET_KEY = "<SECRET_KEY>";

}
``` 

## How To Build

该示例使用了Gradle进行构建,直接使用./gradlew build会自动下载Gradle本身以及相关依赖

```
./gradlew build
./gradlew test
```