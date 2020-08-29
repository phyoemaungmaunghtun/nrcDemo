package com.xan.nrcdemo.data

data class TokenRequestModel(
    var auth: auth
)

data class auth(
    var identity: identity,
    var scope: scope
)

data class identity(
    var methods: List<String>,
    var password: password
)

data class password(
    var user: user
)

data class user(
    var name: String,
    var password: String,
    var domain:domain
)

data class domain(
    var name: String
)

data class scope(
    var project: project
)

data class project(
    var name: String
)