
const gql = require('graphql')
const _ = require('lodash')
const {
    GraphQLString,
    GraphQLList,
    GraphQLInt,
    GraphQLNonNull,
    GraphQLObjectType,
    GraphQLSchema
} = gql

const crs = [
    { id: 0, tl: 'cr0', bd: 0, pr: 100, ag: 3 },
    { id: 1, tl: 'cr1', bd: 0, pr: 200, ag: 2 },
    { id: 2, tl: 'cr2', bd: 1, pr: 300, ag: 1 }
]

const brs = [
    { id: 0, nm: 'br0' },
    { id: 1, nm: 'br1' }
]

const cr = new GraphQLObjectType({
    name: 'cr',
    fields: () => ({
        id: { type: new GraphQLNonNull(GraphQLInt) },
        tl: { type: new GraphQLNonNull(GraphQLString) },
        br: {
            type: new GraphQLNonNull(br),
            resolve: (p, a) => _.find(brs, {
                id: _.find(crs, { id: p.id }).bd
            })
        },
        pr: { type: new GraphQLNonNull(GraphQLInt) },
        ag: { type: new GraphQLNonNull(GraphQLInt) }
    })
})

const br = new GraphQLObjectType({
    name: 'br',
    fields: () => ({
        id: { type: new GraphQLNonNull(GraphQLInt) },
        nm: { type: new GraphQLNonNull(GraphQLString) }
    })
})

const qr = new GraphQLObjectType({
    name: 'rtq',
    fields: {
        cr: {
            type: cr,
            args: { id: { type: GraphQLInt } },
            resolve: (p, a) => _.find(crs, { id: a.id })
        },
        crs: {
            type: new GraphQLList(cr),
            resolve: (p, a) => crs
        },
        brs: {
            type: new GraphQLList(br),
            resolve: (p, a) => brs
        }
    }
})

const mt = new GraphQLObjectType({
    name: 'mt',
    fields: {
        ddc: {
            type: cr,
            args: {
                id: { type: new GraphQLNonNull(GraphQLInt) },
                tl: { type: new GraphQLNonNull(GraphQLString) },
                bd: { type: new GraphQLNonNull(GraphQLInt) },
                pr: { type: new GraphQLNonNull(GraphQLInt) },
                ag: { type: new GraphQLNonNull(GraphQLInt) }
            },
            resolve: (p, a) => crs[crs.push(a) - 1]
        },
        ddb: {
            type: br,
            args: {
                id: { type: new GraphQLNonNull(GraphQLInt) },
                nm: { type: new GraphQLNonNull(GraphQLString) }
            },
            resolve: (p, a) => brs[brs.push(a) - 1]
        }
    }
})

module.exports = new GraphQLSchema({
    query: qr,
    mutation: mt
})
