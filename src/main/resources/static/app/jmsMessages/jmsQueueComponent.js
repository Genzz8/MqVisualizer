angular.
  module('jmsMessageApp').
  component('queueList', {
        templateUrl: '../app/jmsMessages/jmsQueueTemplate.html',
        controller: function JmsQueueController(Messages, $state ) {

            var self = this;

            self.queues = Messages.query();
        
            self.setQueue = setQueue;
            
            function setQueue(index) {
                self.paramQueue = self.queues[index].paramQueue;
                self.dataQueue = self.queues[index].dataQueue;
                self.charset = self.queues[index].charset;
                
            }

            function getList() {
                
                Messages.getMessages({ 
                    paramQueue:self.paramQueue,
                    dataQueue:self.dataQueue,
                    charset:self.charset
                }, function(res){
                    var messages = res.data;
                    if(messages.length <= 0)
                    	return
                    $state.go('jmsList', {data:messages});
                }, function(error) {
                   console.log(error);
                  });
            }

            self.getList = getList;



        }
    });