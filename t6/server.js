
const exp = require('express')
const app = exp()
const { graphqlHTTP } = require('express-graphql')
const sch = require('./sch')

app.use('/g', graphqlHTTP({ schema: sch, graphiql: true }))
app.get('/', (q, s) => s.sendFile(__dirname + '/dx.html'))
app.listen(1234)
