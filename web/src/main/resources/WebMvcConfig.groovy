beans {
    xmlns([mvc: 'http://www.springframework.org/schema/mvc'])
    mvc.'annotation-driven'()
    importBeans('WebConfig.groovy')
}