
const gql = require('graphql')
const lodash = require('lodash')
const {
    GraphQLString,
    GraphQLList,
    GraphQLInt,
    GraphQLNonNull,
    GraphQLObjectType,
    GraphQLSchema
} = gql

const cars = [
    { id: 0, title: 'car0', brandId: 0, price: 100, age: 3 },
    { id: 1, title: 'car1', brandId: 0, price: 200, age: 2 },
    { id: 2, title: 'car2', brandId: 1, price: 300, age: 1 }
]

const brands = [
    { id: 0, name: 'brand0' },
    { id: 1, name: 'brand1' }
]

const Car = new GraphQLObjectType({
    name: 'Car',
    fields: () => ({
        id: { type: new GraphQLNonNull(GraphQLInt) },
        title: { type: new GraphQLNonNull(GraphQLString) },
        brand: {
            type: new GraphQLNonNull(Brand),
            resolve: (parameter, _) => lodash.find(brands, {
                id: lodash.find(cars, { id: parameter.id }).brandId
            })
        },
        price: { type: new GraphQLNonNull(GraphQLInt) },
        age: { type: new GraphQLNonNull(GraphQLInt) }
    })
})

const Brand = new GraphQLObjectType({
    name: 'Brand',
    fields: () => ({
        id: { type: new GraphQLNonNull(GraphQLInt) },
        name: { type: new GraphQLNonNull(GraphQLString) }
    })
})

const queryDescription = new GraphQLObjectType({
    name: 'rootQuery',
    fields: {
        car: {
            type: Car,
            args: { id: { type: GraphQLInt } },
            resolve: (parameter, args) => lodash.find(cars, { id: args.id })
        },
        cars: {
            type: new GraphQLList(Car),
            resolve: (_, __) => cars
        },
        brands: {
            type: new GraphQLList(Brand),
            resolve: (_, __) => brands
        }
    }
})

const mutationDescription = new GraphQLObjectType({
    name: 'mutation',
    fields: {
        addCar: {
            type: Car,
            args: {
                id: { type: new GraphQLNonNull(GraphQLInt) },
                title: { type: new GraphQLNonNull(GraphQLString) },
                brandId: { type: new GraphQLNonNull(GraphQLInt) },
                price: { type: new GraphQLNonNull(GraphQLInt) },
                age: { type: new GraphQLNonNull(GraphQLInt) }
            },
            resolve: (_, args) => cars[cars.push(args) - 1]
        },
        addBrand: {
            type: Brand,
            args: {
                id: { type: new GraphQLNonNull(GraphQLInt) },
                name: { type: new GraphQLNonNull(GraphQLString) }
            },
            resolve: (_, args) => brands[brands.push(args) - 1]
        }
    }
})

module.exports = new GraphQLSchema({
    query: queryDescription,
    mutation: mutationDescription
})
