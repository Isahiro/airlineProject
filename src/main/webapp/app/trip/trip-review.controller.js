'use strict';

(() => {
  angular
    .module('airline.trip')
    .controller('TripReviewController', TripReviewController)

  TripReviewController.$inject = [
    'accessService', 'tripService', '$state', '$scope', '$http', '$log'
  ]

  function TripReviewController (
    accessService, tripService, $state, $scope, $http, $log
  ) {
    this.trips
    this.flights = []

    tripService
      .getTripsById(accessService.currentUser.id)
      .then(trips => this.trips = trips)
      .then(() => {
        this.trips
          .forEach(this.getTickets)
      })

    this.cancelTrip = (trip) => {
      tripService
        .cancelTrip(trip)
        .then(trips => {
          this.trips = trips
        })
    }

    this.updateTrip = (origin, destination, trip) => {
      tripService.setOrigin(origin)
      tripService.setDestination(destination)
      tripService.setTrip(trip)
      $state.go('planTrip')
    }

    this.getTickets = (trip) => {
      trip.tickets.forEach(this.getFlightFromModel)
    }

    this.getFlightFromModel = (ticket) => {
      $log.debug('calling getFlightFromModel ' + ticket.flightId)
      $http
        .get('./api/tickets/flights/' + ticket.flightId)
        .then(response => response.data)
        .then(flight => {
          $log.debug(flight)
          this.flights.push(flight)
        })
        .then(() => {
          $log.debug(this.flights)
        })
    }

    this.getFlight = (ticket) => {
      for (let i = 0; i < this.flights.length; i++) {
        $log.debug(this.flights[i])
        if (this.flights[i].flightId === ticket.flightId) {
          return this.flights[i]
        }
      }
    }
  }
})()
