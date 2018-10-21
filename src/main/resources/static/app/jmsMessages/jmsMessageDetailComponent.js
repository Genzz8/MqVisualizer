angular.
  module('jmsMessageApp').
  component('messageDetail', {
        templateUrl: '../app/jmsMessages/messageDetailTemplate.html',
        controller: function JmsMessagesDetailController( $stateParams ) {
            var self = this;
            self.message = $stateParams.data;
            debugger;
            
        }
    });