beans {
    xmlns([mvc: 'http://www.springframework.org/schema/mvc'])
    mvc.'annotation-driven'()
    importBeans('classpath:WebConfig.groovy')
}