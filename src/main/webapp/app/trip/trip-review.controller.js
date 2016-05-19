'use strict';

(() => {
  angular
    .module('airline.trip')
    .controller('TripReviewController', TripReviewController)

  TripReviewController.$inject = [
    'userTrips', 'tripService', '$state', '$scope', '$http'
  ]

  function TripReviewController (
    userTrips, tripService, $state, $scope, $http
  ) {
    this.trips = userTrips
    this.flights

    this.trips
      .forEach(this.getTickets)
      .forEach(this.getFlightFromModel)

    this.cancelTrip = (trip) => {
      tripService
        .cancelTrip(trip)
        .then(trips => {
          this.trips = trips
          $scope.apply()
        })
    }

    this.updateTrip = (origin, destination, trip) => {
      tripService.setOrigin(origin)
      tripService.setDestination(destination)
      tripService.setTrip(trip)
      $state.go('planTrip')
    }

    this.getTickets = (trip) => {
      return trip.tickets
    }

    this.getFlightFromModel = (ticket) => {
      $http
        .get('./api/trips/flights/' + ticket.flightId)
        .then(response => response.data)
        .then(flight => this.flights.push(flight))
    }

    this.getFlight = (ticket) => {
      this.flights.forEach(function (flight) {
        if (flight.flightId === ticket.flightId) {
          return flight
        }
      })
    }
  }
})()
