PROGETTO INGSW - MICHELE CANNIZZARO - O55000388

Nome progetto: Whistler

-----DOCUMENTAZIONE-----
1) All'interno della cartella "Documentazione" si trovano i documenti di Ideazione, Visione e 
  delle varie iterazioni di Elaborazione suddivisi per cartelle. 

2) Ogni cartella di Elaborazione contiene al suo interno:
    - DCD dell'iterazione in esame in formato .png
    - il Modello di Dominio dell'iterazione in esame in formato .png
    - la relazione relativa alla fase di Elaborazione relativa 
      all'iterazione in esame 
    - il file Whistler.asta contentente (DCD - Modello dominio per caso 
      d'uso, Diagrammi di sequenza)
    - un file .zip contente il codice relativo all'iterazione in esame
    
3) La cartella "Doc_Completa" contiene oltre alla documentazione finale (ripetizione di quella
   presente nella cartella Elaborazione_4) il documento di Visione aggiornato ed il documento 
   "Elaborazione_it_1_2_3_4.pdf" che raccoglie in un unico file tutte le elaborazioni presenti 
   nelle varie cartelle di elaborazione.

-----PER ESEGUIRE IL PROGETTO-----
1) E' necessario disporre di Java:
		openjdk 17.0.5 2022-10-18
		OpenJDK Runtime Environment (build 17.0.5+8-Ubuntu-2ubuntu120.04)
		OpenJDK 64-Bit Server VM (build 17.0.5+8-Ubuntu-2ubuntu120.04, mixed mode, sharing)

2) Non è necessario avere mysql installato, poiché il progetto out of the box utilizza un'istanza
   mysql in Versione 8 su piattaforma Microsoft Azure con licenza studenti offerta dall'UniCT
   
   si tenga presente però che le performance non sono elevate per cui l'esecuzione dei JUnit test 
   risulterà essere lenta, non si sono riscontrati eccessivi rallentamenti durante l'esecuzione
   dell'applicativo. (Eseguendo mysql in locale il problema non sussiste)
   
   2.1) se si vuole ovviare all'uso di mysql su Azure è necessario installare mysql alla versione 8:
   	mysql  Ver 8.0.31-0ubuntu0.20.04.2 for Linux on x86_64 ((Ubuntu))
   	
   	e necessario: - creare un database di nome: whistler_db
   		      - de-commentare le righe [14-16] del file "whistler/util/hibernate.cfg.xml"
   		        eliminando <!-- e -->
   		      		 		<!--<property name="hibernate.connection.url">
							jdbc:mysql://localhost:3306/whistler_db?useSSL=false
						    </property>-->
		     - commentare le righe [17-19] del file "whistler/util/hibernate.cfg.xml"
		       inserendo  <!-- e -->
		     				<property name="hibernate.connection.url">
							jdbc:mysql://whistler-ingsw.mysql.database.azure.com:3306/whistler_db?useSSL=false
						</property>
						
		     - connection.username = ingsw_proj 
		       connection.password = Whistlerdbpass2!
		       
3) Per via dei limiti dell'interfaccia grafica è necessario aumentare la dimensione della finestra del 
   terminale fin quando non si ha una visione ottimale delle scritte, all'aumentare dei post pubblicati
   sarà necessario scrollare il terminale al fine di vedere tutte le informazioni disponibili.

4) Se si utilizza mysql su Azure, sono già presenti nel database due utenti con cui è possibile loggarsi:
	nickname: snowden			nickname: assange
	password: ingsw2022			password: ingsw2022

5) Affinché alla pubblicazione di un post venga inviata una notifica ai follower anche via mail è necessario
   che l'indirizzo e-mail fornito in fase di registrazione o nella scheda profile>settings sia valido
