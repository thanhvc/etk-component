Note:

Data injector is used to create data for performance test benchmark. 
This page will describe which Data Injectors are implemented in your project
 and how to use them

How to:

1. The POM.xml configuration.
These classes are placed in library:
     <dependency>
      <groupId>org.etk.extras</groupId>
      <artifactId>exo.etk.extras.benches</artifactId>
      <version>${org.etk.extras.version}</version>
      <scope>provided</scope>
    </dependency>

2. The DataInjector abstract class.

public abstract class DataInjector extends BaseComponentPlugin {
 
  /**
   * check whether data is initialized or not.
   *
   * @return true if data is initialized.
   */
  public abstract boolean isInitialized();
 
  /**
   * transform parameters set by user to variables.
   * @param initParams
   */
  public abstract void initParams(InitParams initParams);
 
  /**
   * inject data
   * @throws Exception
   */
  public abstract void inject() throws Exception;
 
  /**
   * reject data
   * @throws Exception
   */
  public abstract void reject() throws Exception;

3. Configuration

To activate Data injector service, the service need to be registered to portal container by following configuration:
<component>
    <type>org.etk.extras.bench.DataInjectorService</type>
</component>

4. Your Data Injector

The configuration for forum data injector:

<external-component-plugins>
    <target-component>org.exoplatform.services.bench.DataInjectorService</target-component>
    <component-plugin>
      <name>ForumDataInjector</name>
      <set-method>addInjector</set-method>
      <type>org.exoplatform.forum.bench.ForumDataInjector</type>
      <description>inject data for Forum</description>
      <init-params>
        <value-param>
          <name>mC</name> <!-- maximum number of categories -->
          <value>10</value>
        </value-param>
        <value-param>
          <name>mF</name> <!-- maximum number of forums -->
          <value>3</value>
        </value-param>
        <value-param>
          <name>mT</name> <!-- maximum number of topics -->
          <value>50</value>
        </value-param>
        <value-param>
          <name>mP</name> <!-- maximum number of posts -->
          <value>50</value>
        </value-param>
        <value-param>
          <name>rand</name> <!-- randomize or not -->
          <value>true</value>
        </value-param>
      </init-params>
    </component-plugin>
</external-component-plugins>

5. Access to execute via Rest

How to use

Users use RESTful service to request to inject or reject data. The format of request link is:
http://{domain}/{rest}/bench/{inject|reject}/{pluginName}?[params]

For example,

after registering wiki injector plugin as above, user can request an injecting as following: http://localhost:8080/rest-ksdemo/bench/inject/WikiDataInjector?mP=10&mA=100&mD=1&rand=false&wo=classic&wt=portal with 10 pages per depths created, 100 kb data per an attachment, 1 depth for wiki portal classic.

To reject such created data, request this link: http://localhost:8080/rest-ksdemo/bench/reject/WikiDataInjector.
