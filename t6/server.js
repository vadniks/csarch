
const exp = require('express')
const app = exp()
const { graphqlHTTP } = require('express-graphql')
const schema = require('./schema')

app.use('/g', graphqlHTTP({ schema: schema, graphiql: true }))
app.get('/', (_, response) => response.sendFile(__dirname + '/index.html'))
app.listen(1234)
