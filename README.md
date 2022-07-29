<h1>AWSCassandraDemo</h1>
<h3>Spring boot start with Mysql(jpa) and AWS keyspace</h3>
<p>This project is a simple demo for AWS keyspace. For more application , I also integrate with spring JAP and
    Mysql </p>
<ul>
    <li>
        <h4>AWS Prerequisites</h4>
        <p>You need to generate a truststore.jks file and application.conf</p>
        <p>Follow Link <a href="https://docs.aws.amazon.com/keyspaces/latest/devguide/dsbulk-upload-prequs.html">Amazon
            Keyspaces (for Apache Cassandra)</a></p>
        <p>just do it step by step</p>
        <div class="procedure">
            <ol>
                <li>
                    <p>If you have not already done so, sign up for an AWS account by following the
                        steps at <a href="./accessing.html#SettingUp.MCS.SignUpForAWS">Signing up for AWS</a>.
                    </p>
                </li>
                <li>
                    <p>Create credentials by following the steps at <a href="./programmatic.credentials.html">Creating
                        credentials to access Amazon Keyspaces
                        programmatically</a>.
                    </p>
                    <div class="awsdocs-note">
                        <ul>
                            <li>
                                <p>Download the AWS CLI at <a href="https://aws.amazon.com/cli"
                                                              rel="noopener noreferrer" target="_blank"><span>http://aws.amazon.com/cli</span>
                                    <awsui-icon class="awsdocs-link-icon" name="external"><span
                                            class="awsui_icon_h11ix_31bp4_98 awsui_size-normal-mapped-height_h11ix_31bp4_151 awsui_size-normal_h11ix_31bp4_147"><svg
                                            xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" focusable="false"
                                            aria-hidden="true"><path class="stroke-linecap-square" d="M10 2h4v4"></path><path
                                            d="m6 10 8-8"></path><path class="stroke-linejoin-round"
                                                                       d="M14 9.048V14H2V2h5"></path></svg></span>
                                    </awsui-icon>
                                </a>.
                                </p>
                            </li>
                            <li>
                                <p>Follow the instructions for <a
                                        href="https://docs.aws.amazon.com/cli/latest/userguide/installing.html">Installing
                                    the AWS
                                    CLI</a> and <a
                                        href="https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html">Configuring
                                    the AWS CLI</a> in the
                                    <em>AWS Command Line Interface User Guide</em>.</p>
                            </li>
                            <li>
                                <p>Using the AWS CLI, run the following command to generate
                                    service-specific credentials for the user <code class="code">alice</code>, so
                                    that she can access Amazon Keyspaces.
                                </p>
                                <pre class="programlisting"><code>
    aws iam create-service-specific-credential \
    --user-name alice \
    --service-name cassandra.amazonaws.com
</code></pre>
                            </li>
                        </ul>
                        <p>The output looks like the following.</p>
                        <pre><code class="nohighlight hljs">
    <span>{</span>
        "ServiceSpecificCredential":
        <span>{</span>
            "CreateDate": "2019-10-09T16:12:04Z",
            "ServiceName": "cassandra.amazonaws.com",
            <b>"ServiceUserName": "alice-at-111122223333",</b>
            <b>"ServicePassword": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY",</b>
            "ServiceSpecificCredentialId": "ACCAYFI33SINPGJEBYESF",
            "UserName": "alice",
            "Status": "Active"
        <span>}</span>
    <span>}</span>
</code></pre>
                    </div>
                </li>
                <li>
                    <p>Create a JKS trust store file.</p>
                    <ol>
                        <li>
                            <p> Download the Starfield digital certificate using the following command and save
                                <code>sf-class2-root.crt</code> locally or in your home directory.
                            </p>
                            <pre class="programlisting"><code>
    curl https:<span class="hljs-regexp">//</span>certs.secureserver.net<span class="hljs-regexp">/repository/</span>sf-class2-root.crt -O
</code></pre>
                        </li>
                        <li>
                            <p>Convert the Starfield digital certificate into a trustStore file.</p>
                            <pre class="programlisting"><code class="hljs ceylon">
    openssl x509 -outform der -in sf-class2-root.crt -out temp_file.der
    keytool -import -alias cassandra -keystore cassandra_truststore.jks -file temp_file.der
</code></pre>
                            <p>
                                In this step, you need to create a password for the keystore and trust this
                                certificate. The interactive command looks like this.
                            </p>
                            <pre class="programlisting"><code copy="false">
    Enter keystore password:
    Re-enter new password:
    Owner: OU=Starfield Class 2 Certification Authority, O="Starfield Technologies, Inc.", C=US
    Issuer: OU=Starfield Class 2 Certification Authority, O="Starfield Technologies, Inc.", C=US
    Serial number: 0
    Valid from: Tue Jun 29 17:39:16 UTC 2004 until: Thu Jun 29 17:39:16 UTC 2034
    ..........
    ...
    Trust this certificate? [no]:  y
</code></pre>
                        </li>
                    </ol>
                </li>
                <li>
                    <p>Create an <code>application.conf</code> file</p>
                    <pre><code>
    datastax-java-driver {
        basic.request.consistency = LOCAL_QUORUM
        basic.contact-points = [<b>"yours"</b>]
        advanced.auth-provider{
          class = PlainTextAuthProvider
          <b>username = "yours"</b>
          <b>password = "yours"</b>
        }
        basic.load-balancing-policy {
          local-datacenter = <b>"yours ex: ap-northeast-1"</b>
        }
        advanced.ssl-engine-factory {
          class = DefaultSslEngineFactory
          truststore-path = "./src/main/resources/cassandra_truststore.jks"
          truststore-password = ""
          hostname-validation = false
        }
    }
</code></pre>
                </li>
            </ol>
        </div>
    </li>
    <li>
        <h4>Setup your Project</h4>
        <p>put <code>application.conf</code> and <code>cassandra_truststore.jks</code> in resources folder</p>
        <pre><code>
    spring:
      main:
        allow-bean-definition-overriding: true
      http:
        encoding:
          force: true
      jpa:
        open-in-view: true
        properties:
          hibernate:
            dialect: org.hibernate.dialect.MySQL5Dialect
      datasource:
        url: jdbc:mysql://localhost:3306/animal_sys
        username: root
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
      data:
        cassandra:
          config: classpath:application.conf  // if use spring-boot-starter-data-cassandra. it works.
          ssl: true
          keyspace-name: skskeyspace1
          port: 9142
          contact-points: cassandra.ap-northeast-1.amazonaws.com
          username:
          password:
    ssl:
      trustStore:
        trustStoreLocation: /cassandra_truststore.jks
        trustStorePassword:
</code></pre>
    </li>
    <li>
        <h4>Setup Project POM</h4>
        <p>more tips for <code>pom.xml</code></p>
        <p>if you want use mysql comment out follows:</p>
        <pre><code><span style="color:lightgrey">
        <span><--</span>
        <span><</span>dependency<span>></span>
            <span><</span>groupId<span>></span>org.springframework.boot<span><</span>/groupId<span>></span>
            <span><</span>artifactId<span>></span>spring-boot-starter-data-jpa<span><</span>/artifactId<span>></span>
        <span><</span>/dependency<span>></span>
        <span>--></span>
    </span>
</code></pre>
        <p>if you want use spring cassandra auto configure with spring.data.cassandra in properties, add follows</p>
        <pre><code><span style="color:lightgreen">
        <span><</span>dependency<span>></span>
            <span><</span>groupId<span>></span>org.springframework.boot<span><</span>/groupId<span>></span>
            <span><</span>artifactId<span>></span>spring-boot-starter-data-cassandra<span><</span>/artifactId<span>></span>
        <span><</span>/dependency<span>></span>
    </span>
</code></pre>
    </li>
</ul>
