'use strict';

(() => {
  angular
    .module('airline.trip')
    .controller('TripPlannerController', TripPlannerController)

  TripPlannerController.$inject = [
    'tripService', 'accessService', '$scope', '$state', '$log']

  function TripPlannerController (
    tripService, accessService, $scope, $state, $log
  ) {
    this.locations
    this.origin = tripService.getOrigin
    this.destination = tripService.getDestination
    this.errorMessage = undefined
    this.routes

    tripService
      .getLocations()
      .then(locations => this.locations = locations)

    this.setOrigin = (location) => {
      this.origin = location
    }

    this.setDestination = (location) => {
      this.destination = location
    }

    this.findRoutes = () => {
      tripService
        .findRoutes(this.origin, this.destination)
        .then(routes => this.routes = routes)
        .then(() => $scope.apply)
    }

    this.bookRoute = (route) => {
      this.errorMessage = undefined

      tripService
        .bookRoute(this.origin.id, this.destination.id, route)
        .then(trip => {
          if (trip !== undefined) {
            this.origin = undefined
            this.destination = undefined
            this.routes = undefined
            tripService.setTrip(undefined)
            $state.go('reviewTrips')
          } else {
            this.errorMessage = 'There was a problem booking this route. Please try another route'
            this.findRoutes()
          }
        })
    }
  }
})()
