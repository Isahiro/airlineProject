'use strict';

(() => {
  angular
    .module('airline.trip')
    .service('tripService', TripService)

  TripService.$inject = ['accessService', '$http']

  function TripService (accessService, $http) {
    this.storedOrigin
    this.storedDestination
    this.storedTrip

    this.getLocations = () => {
      return $http
        .get('./api/tickets/locations')
        .then(response => response.data)
    }

    this.getTripsById = (id) => {
      return $http
        .get('./api/tickets/trips/' + id)
        .then(response => response.data)
    }

    this.findRoutes = (origin, destination) => {
      return $http
        .get('./api/tickets/' + origin.id + '/' + destination.id)
        .then(response => response.data)
    }

    this.bookRoute = (originId, destinationId, route) => {
      if (this.trip !== undefined) {
        return $http
          .put('./api/tickets/trip/' + accessService.currentUser.id +
              '/' + this.trip.id, route)
          .then(response => response.data)
      } else {
        return $http
          .post('./api/tickets/' + accessService.currentUser.id +
              '/' + originId + '/' + destinationId, route)
          .then(response => response.data)
      }
    }

    this.cancelTrip = (trip) => {
      return $http
        .delete('./api/tickets/trips/' +
            accessService.currentUser.id +
            '/' + trip.id)
        .then(response => response.data)
    }

    this.getOrigin = () => {
      let origin = this.storedOrigin
      this.storedOrigin = undefined
      return origin
    }

    this.getDestination = () => {
      let destination = this.storedDestination
      this.storedDestination = undefined
      return destination
    }

    this.setOrigin = (origin) => {
      this.storedOrigin = origin
    }

    this.setDestination = (destination) => {
      this.storedDestination = destination
    }

    this.setTrip = (trip) => {
      this.storedTrip = trip
    }
  }
})()
