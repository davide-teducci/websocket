import { Component, OnInit } from '@angular/core';
import { RxStomp } from '@stomp/rx-stomp';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environments/environment';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  public dataSocket: Array<String> = [];

  constructor() {

  }
  ngOnInit() {
    const rxStomp = new RxStomp();
    rxStomp.configure({
      webSocketFactory: function () { return new SockJS( environment.socket.factory )},
      heartbeatIncoming: 0,
      heartbeatOutgoing: 20000,
      reconnectDelay: 200,
      //debug: (msg: string): void => {
      //  console.log(new Date(), msg);
      //}
    });
    rxStomp.activate();
    rxStomp.connected$.subscribe(() => {
      rxStomp.stompClient.subscribe(environment.socket.stompClient, this.response)
      rxStomp.publish({ destination: '/message', body: JSON.stringify({ client: 'Message by Client' }) });
    })
  }
  public response = (data) => {
    console.log(data);
    this.dataSocket.push(data.body);

  }
}
