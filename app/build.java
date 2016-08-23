import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Properties;

public class build extends groovy.lang.Script {
    public static void main(String[] args) {
        new build(new groovy.lang.Binding(args)).run();
    }

    public Object run() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(1);
        map.put("plugin", "com.android.application");
        apply(map);


        android(new groovy.lang.Closure<Object>(this, this) {
            public Object doCall(Object it) {
                signingConfigs(new Object[]{new groovy.lang.Closure<com.android.build.gradle.internal.dsl.SigningConfig>(build.this, build.this) {
                    public com.android.build.gradle.internal.dsl.SigningConfig doCall(Object it) {
                        return config(new groovy.lang.Closure<Object>(build.this, build.this) {
                            public Object doCall(Object it) {
                                setKeyAlias(new Object[]{"xobyx"});
                                setKeyPassword(new Object[]{"13011221"});
                                setStoreFile(new Object[]{file("C:/Users/xobyx/AndroidStudioProjects/XContactj2/pkey.jks")});
                                return setStorePassword(new Object[]{"13011221"});
                            }

                            public Object doCall() {
                                return doCall(null);
                            }

                        });
                    }

                    public SigningConfig doCall() {
                        return doCall(null);
                    }

                }});
                lintOptions(new groovy.lang.Closure<Object>(build.this, build.this) {
                    public Object doCall(Object it) {
                        setCheckReleaseBuilds(new Object[]{false});
                        setAbortOnError(new Object[]{false});
                        return disable(new Object[]{"InvalidPackage"});
                    }

                    public Object doCall() {
                        return doCall(null);
                    }

                });
                compileSdkVersion(21);
                buildToolsVersion("21.1.2");
                Properties versionProps = new Properties();
                File versionPropsFile = file("version.properties");
                if (versionPropsFile.exists())
                    versionProps.load(new FileInputStream(versionPropsFile));
                final Object object = versionProps.get("VERSION_CODE");
                final Object code = org.codehaus.groovy.runtime.DefaultGroovyMethods.invokeMethod((org.codehaus.groovy.runtime.DefaultGroovyMethods.asBoolean(object) ? object : "0"), "toInteger", new Object[0]) + 1;
                versionProps.put("VERSION_CODE", code.toString());
                versionProps.store(org.codehaus.groovy.runtime.ResourceGroovyMethods.newWriter(versionPropsFile), null);
                defaultConfig(new groovy.lang.Closure<Object>(build.this, build.this) {
                    public Object doCall(com.android.build.gradle.internal.dsl.ProductFlavor it) {
                        setApplicationId(new Object[]{"xobyx.xcontactj"});
                        minSdkVersion(15);
                        targetSdkVersion(21);
                        setVersionCode((Object[]) code);
                        setVersionName(new Object[]{"1.1"});
                        setMultiDexEnabled(new Object[]{true});
                        return setSigningConfig(build.this.getBinding().getProperty("signingConfigs").config);
                    }

                    public Object doCall() {
                        return doCall(null);
                    }

                });
                buildTypes(new groovy.lang.Closure<com.android.build.gradle.internal.dsl.BuildType>(build.this, build.this) {
                    public com.android.build.gradle.internal.dsl.BuildType doCall(org.gradle.api.NamedDomainObjectContainer<com.android.build.gradle.internal.dsl.BuildType> it) {
                        release(new groovy.lang.Closure<Object>(build.this, build.this) {
                            public Object doCall(Object it) {
                                setMinifyEnabled(new Object[]{false});
                                proguardFiles(invokeMethod("getDefaultProguardFile", new Object[]{"proguard-android.txt"}), "proguard-rules.pro", "xcontactj-proguard.pro");
                                setSigningConfig(build.this.getBinding().getProperty("signingConfigs").config);
                                return setVersionNameSuffix(new Object[]{"-(" + build.this.getBinding().getProperty("defaultConfig").versionCode + ")-beta-aptoide"});
                            }

                            public Object doCall() {
                                return doCall(null);
                            }

                        });
                        return debug(new groovy.lang.Closure<Object>(build.this, build.this) {
                            public Object doCall(Object it) {
                                return proguardFile(new Object[]{"C:/Users/xobyx/AndroidStudioProjects/XContactj2/app/xcontactj-proguard.pro"});
                            }

                            public Object doCall() {
                                return doCall(null);
                            }

                        });
                    }

                    public BuildType doCall() {
                        return doCall(null);
                    }

                });
                productFlavors(new groovy.lang.Closure<Void>(build.this, build.this) {
                    public void doCall(org.gradle.api.NamedDomainObjectContainer<com.android.build.gradle.internal.dsl.ProductFlavor> it) {
                    }

                    public void doCall() {
                        doCall(null);
                    }

                });
            }

            public void doCall() {
                doCall(null);
            }

        });

        dependencies(new groovy.lang.Closure<Object>(this, this) {
            public Object doCall(Object it) {
                LinkedHashMap<String, Serializable> map1 = new LinkedHashMap<String, Serializable>(2);
                map1.put("dir", "libs");
                map1.put("include", new ArrayList<String>(Arrays.asList("*.jar")));
                compile(new Object[]{fileTree(map1)});
                //compile 'frankiesardo:icepick:3.2.0'
                //provided 'frankiesardo:icepick-processor:3.2.0'
                compile(new Object[]{"com.android.support:appcompat-v7:22.2.0"});
                compile(new Object[]{"com.android.support:design:22.2.0"});
                compile(new Object[]{"com.android.support:recyclerview-v7:22.2.0"});
                compile(new Object[]{"com.google.android.gms:play-services-analytics:8.1.0"});
                compile(new Object[]{"com.google.android.gms:play-services-gcm:8.1.0"});
                compile(new Object[]{"com.googlecode.ez-vcard:ez-vcard:0.9.6"});
                compile(new Object[]{"com.google.code.gson:gson:2.4"});
                compile(new Object[]{"com.koushikdutta.ion:ion:1.1.7"});
                compile(new Object[]{"com.vdurmont:emoji-java:1.1.1"});
                compile(new Object[]{"com.github.lzyzsd:circleprogress:1.1.0"});
                compile(new Object[]{"com.klinkerapps:android-chips:1.0.0"});
                compile(new Object[]{"com.jakewharton:butterknife:7.0.1"});
                compile(new Object[]{"me.leolin:ShortcutBadger:1.1.1"});
                return compile(new Object[]{"com.android.support:support-v13:22.2.0"});
            }

            public Object doCall() {
                return doCall(null);
            }

        });


        return null;

    }

    public build(Binding binding) {
        super(binding);
    }

    public build() {
        super();
    }
}
