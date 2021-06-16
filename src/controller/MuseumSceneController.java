package controller;

import code.*;
import gui.SingleVisitor;
import gui.TicketGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.concurrent.Task;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import static code.MuseumController.*;

public class MuseumSceneController implements Initializable {
    @FXML
    public TableView<SingleVisitor> visitor_table;

    @FXML
    public TableColumn<SingleVisitor, String> visitor_startTime;

    @FXML
    public TableColumn<SingleVisitor, String> visitor_ticketID;

    @FXML
    public TableColumn<SingleVisitor, String> visitor_stayDuration;

    @FXML
    public TableColumn<SingleVisitor, String> visitor_entrance;

    @FXML
    public TableColumn<SingleVisitor, String> visitor_exit;

    @FXML
    public TableColumn<SingleVisitor, String> visitor_expireTime;

    public static ObservableList<SingleVisitor> visitorList = FXCollections.observableArrayList();

    @FXML
    private TableView<TicketGUI> ticket_table;

    @FXML
    private TableColumn<TicketGUI, String> ticket_time;

    @FXML
    private TableColumn<TicketGUI, String> ticket_idSold;

    ObservableList<TicketGUI> ticketList = FXCollections.observableArrayList();

    public void updateTicketList(String time, String id) {
        TicketGUI newSold = new TicketGUI(time, id);
        if (!ticket_table.getItems().contains(newSold)) {
            ticketList.add(newSold);
        }
        ticket_table.setItems(ticketList);
    }

    @FXML
    public Button startBtn;

    @FXML
    public Text selectedCaseTitle;

    @FXML
    public ScrollPane topMessages;

    @FXML
    public Text messageHere;
    public static String messageHere_str = " ";
    public String messageHere_str_temp = " ";
    public String messageHere_str_accumulate = "";

    @FXML
    public Text simulatingClock;

    @FXML
    public Text totalTicketSold;

    @FXML
    public Text totalVisitorEntered;

    @FXML
    public ProgressIndicator percentageInside;

    @FXML
    public Text totalVisitorInside;

    @FXML
    public Label visitorID;
    public static String currentEnteringVisitor = "";
    public String currentEnteringVisitor_temp = " ";
    public static String currentEnteringVisitor_stayDuration = "";
    public static String currentEnteringVisitor_entrance = "";
    public static String currentEnteringVisitor_turnstilenum = "";

    public static String currentLeavingVisitor = "";
    public String currentLeavingVisitor_temp = " ";
    public static String currentLeavingVisitor_exit = "";
    public static String currentLeavingVisitor_turnstilenum = "";

    @FXML
    public Tooltip visitorID_stayDuration;
    //    visitorID_stayDuration.setShowDelay(Duration.seconds(1));

    @FXML
    public GridPane imMuseum;

    @FXML
    public StackPane musuemlaide;

    @FXML
    private Text nt1;

    @FXML
    private Text nt2;

    @FXML
    private Text nt3;

    @FXML
    private Text nt4;

    @FXML
    private Text st1;

    @FXML
    private Text st2;

    @FXML
    private Text st3;

    @FXML
    private Text st4;

    @FXML
    private Text et1;

    @FXML
    private Text et2;

    @FXML
    private Text et3;

    @FXML
    private Text et4;

    @FXML
    private Text wt1;

    @FXML
    private Text wt2;

    @FXML
    private Text wt3;

    @FXML
    private Text wt4;

    private Service<Void> simulatingClockThread;
    private Service<Void> visitorThread;
    private Service<Void> visitorGUIThread;
    int selectedCaseNum;
    public static boolean started = false;

    public void initData(TextField t) {
        selectedCaseNum = Integer.parseInt(t.getText());
        selectedCaseTitle.setText(MuseumController.getOptions(Integer.parseInt(t.getText())));
    }

//    public ObservableList<ticket_gui> getTicketList() {
//        ObservableList<ticket_gui> ticketList = FXCollections.observableArrayList();
//        ticketList.add(new ticket_gui("0000", "dsadfsf"));
//        return ticketList;
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ticket_time.setCellValueFactory(new PropertyValueFactory<>("ticket_time"));
        ticket_idSold.setCellValueFactory(new PropertyValueFactory<>("ticket_idSold"));
        percentageInside.progressProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.doubleValue() >= 1.0) {
                Text doneText = (Text) percentageInside.lookup(".percentage");
                doneText.setText("Full");
            }
        });
//        percentageInside.progressProperty().addListener((ov, oldValue, newValue) -> {
//            Text text = (Text) percentageInside.lookup(".percentage");
//            if (text != null && text.getText().equals("Done")) {
//                text.setText("Full!");
//            }
//        });
        visitor_startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        visitor_ticketID.setCellValueFactory(new PropertyValueFactory<>("ticketID"));
        visitor_stayDuration.setCellValueFactory(new PropertyValueFactory<>("stayDuration"));
        visitor_entrance.setCellValueFactory(new PropertyValueFactory<>("entrance"));
        visitor_exit.setCellValueFactory(new PropertyValueFactory<>("exit"));
        visitor_expireTime.setCellValueFactory(new PropertyValueFactory<>("expireTime"));
        nt1.setText("");
        nt2.setText("");
        nt3.setText("");
        nt4.setText("");
        st1.setText("");
        st2.setText("");
        st3.setText("");
        st4.setText("");
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        wt1.setText("");
        wt2.setText("");
        wt3.setText("");
        wt4.setText("");
        visitorID.setText("");


//        ticket_table.setItems(getTicketList());
//        (ticketQ.toString()).setCellValueFactory(new PropertyValueFactory<RandomTicketing,String>("ticketQ"));
    }

    public void startSimulating(ActionEvent actionEvent) {
        ArrayList<String[]> allPurchases = new ArrayList<String[]>();
        if (selectedCaseNum != 1) {
            //for case 2-4
            String fileName = "";

            switch (selectedCaseNum) {
                case 2:
                    fileName += "Exceed100Visitors.txt";
                    break;
                case 3:
                    fileName += "Exceed900Tickets.txt";
                    break;
                case 4:
                    fileName += "Exceed6pm.txt";
                    break;
            }

            TextReader tr = new TextReader(fileName);
            allPurchases = tr.readTextFile();
        }
        Clock clock = new Clock();
        Thread clockThread = new Thread(clock);
        clockThread.start();
        simulatingClockThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
//                        System.out.println(Clock.getCurrentTime().before(Clock.getEntranceOpenTime()));
                        while (true) {
                            if (Clock.print != null) {
                                started = true;
                                updateMessage(Clock.print);
                                if (messageHere_str != messageHere_str_temp) {
                                    String toAnnounce = Clock.print + " " + messageHere_str;
                                    messageHere_str_temp = messageHere_str;
                                    messageHere_str_accumulate += toAnnounce + "\n";
                                    messageHere.setText(messageHere_str_accumulate);
                                }
                                if (selectedCaseNum == 1) {
                                    if (RandomTicketing.ticketingPrint != null) {
                                        totalTicketSold.setText(String.valueOf(RandomTicketing.totalTicketSold));
                                        updateTicketList(Clock.print, RandomTicketing.ticketingPrint);
                                    }
                                } else {
                                    // for case 2-4
                                    if (Ticketing.ticketingPrint != null) {
                                        totalTicketSold.setText(String.valueOf(Ticketing.totalTicketSold));
                                        updateTicketList(Clock.print, Ticketing.ticketingPrint);
                                    }
                                }
                            }
                        }
//                        return null;
                    }
                };
            }
        };
        simulatingClockThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("clock done");
                simulatingClock.textProperty().unbind();
            }
        });
        simulatingClock.textProperty().bind(simulatingClockThread.messageProperty());
        simulatingClockThread.restart();

        while (true) {
            if (started) {
                if (selectedCaseNum == 1) {
                    Thread randomTicketingThread = new Thread(new RandomTicketing(MuseumController.ticketQ));
                    randomTicketingThread.start();
                } else {
                    // for case 2-4
                    Thread ticketingThread = new Thread(new Ticketing(allPurchases, ticketQ));
                    ticketingThread.start();
                }
//                    messageHere_str += Clock.print + " " + RandomTicketing.printCounterOpenMsg();
//                    messageHere.setText(messageHere_str);
                break;
            }
        }
        Museum museum = new Museum();
        visitorThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        while (Clock.getCurrentTime().before(Clock.getEntranceOpenTime())) {
                            //wait
                        }
                        Thread entranceThread = new Thread(new Entrance(ticketQ, museum, visitorQ));
                        messageHere_str = printEntranceOpenMsg();
                        entranceThread.start();

                        Thread exitThread = new Thread(new Exit(visitorQ, museum, exitQ));
                        exitThread.start();
                        while (entranceThread.isAlive() || exitThread.isAlive()) {
                            totalVisitorEntered.setText(String.valueOf(Museum.totalNumOfVisitors));
                            totalVisitorInside.setText(String.valueOf(Museum.numOfVisitorsInMuseum) + "/100");
                            percentageInside.setProgress((double) Museum.numOfVisitorsInMuseum / 100);
//                                Platform.runLater(new Runnable() {
//                                    @Override
//                                    public void run() {
                            if (currentEnteringVisitor != "" && currentEnteringVisitor != currentEnteringVisitor_temp) {
                                visitor_table.setItems(visitorList);
//                                    System.out.println("ghgh: " + currentEnteringVisitor + " " + currentEnteringVisitor_stayDuration);
                                String e = currentEnteringVisitor_entrance;
                                String t = currentEnteringVisitor_turnstilenum;
                                String v = currentEnteringVisitor;
                                String s = currentEnteringVisitor_stayDuration;
                                setVstIn(e, t, v);
//                                setVstIn(currentEnteringVisitor_entrance, currentEnteringVisitor_turnstilenum, currentEnteringVisitor);
//                                visitorID.setText(currentEnteringVisitor);
//                                Tooltip stayDuration = new Tooltip(String.valueOf(currentEnteringVisitor_stayDuration));
//                                visitorID.setTooltip(stayDuration);
//                                Random random = new Random();
//                                Label vst = new Label(v);
////////                                            vst.setLayoutX(100d);
////////                                            vst.setLayoutY(100d);
//////////                                            vst.setTranslateX(random.nextInt(800));
//////////                                            vst.setTranslateY(random.nextInt(600));
////                                Tooltip stayDuration = new Tooltip(String.valueOf(currentEnteringVisitor_stayDuration));
//                                Tooltip stayDuration = new Tooltip(String.valueOf(s));
//                                vst.setTooltip(stayDuration);
//                                System.out.println(vst);
////                                currentEnteringVisitor_temp = currentEnteringVisitor;
//                                currentEnteringVisitor_temp = v;
//                                System.out.println("Dd"+currentEnteringVisitor_temp);
//                                            GridPane.setConstraints(vst, random.nextInt(10), random.nextInt(10));
//                                            imMuseum.getChildren().add(vst);
////
//                                    musuemlaide.getChildren().add(vst);
//                                System.out.println("children:"+musuemlaide.getChildren());


                            }
                            if (currentLeavingVisitor != "" && currentLeavingVisitor != currentLeavingVisitor_temp) {
                                setVstOut(currentLeavingVisitor_exit, currentLeavingVisitor_turnstilenum, currentLeavingVisitor);
                                currentLeavingVisitor_temp = currentLeavingVisitor;
                            }
//                                    }
//                                });
                        }
                        return null;
                    }
                };
            }
        };
        visitorThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("visitor done");
            }
        });
        visitorThread.restart();


        //
//            visitorGUIThread = new Service<Void>() {
//                @Override
//                protected Task<Void> createTask() {
//                    return new Task<Void>() {
//                        @Override
//                        protected Void call() throws Exception {
//                            while (true) {
//                                if (currentVisitor != "" && currentVisitor != currentVisitor_temp) {
//                                    Label vst = new Label(currentVisitor);
//                                    Tooltip stayDuration = new Tooltip(String.valueOf(SingleVisitor.getStayDuration()));
//                                    vst.setTooltip(stayDuration);
//                                    imMuseum.getChildren().addAll(vst);
//                                    currentVisitor_temp = currentVisitor;
//                                }
//                            }
////                            return null;
//                        }
//                    };
//                }
//            };
//            visitorGUIThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent event) {
//                    System.out.println("visitor done");
//                }
//            });
////             id.textProperty().bind(visitorGUIThread.messageProperty());
//            visitorGUIThread.restart();
    }

    public void setVstIn(String currentDirection, String currentTurnstile, String currentVst) {
        if (currentDirection.equalsIgnoreCase("north")) {
            if (currentTurnstile.contains("1")) {
                nt1.setText(currentVst);
            } else if (currentTurnstile.contains("2")) {
                nt2.setText(currentVst);
            } else if (currentTurnstile.contains("3")) {
                nt3.setText(currentVst);
            } else if (currentTurnstile.contains("4")) {
                nt4.setText(currentVst);
            }
        } else if (currentDirection.equalsIgnoreCase("south")) {
            if (currentTurnstile.contains("1")) {
                st1.setText(currentVst);
            } else if (currentTurnstile.contains("2")) {
                st2.setText(currentVst);
            } else if (currentTurnstile.contains("3")) {
                st3.setText(currentVst);
            } else if (currentTurnstile.contains("4")) {
                st4.setText(currentVst);
            }
        }
    }


    public void setVstOut(String currentDirection, String currentTurnstile, String currentVst) {
        if (currentDirection.equalsIgnoreCase("east")) {
            if (currentTurnstile.contains("1")) {
                et1.setText(currentVst);
            } else if (currentTurnstile.contains("2")) {
                et2.setText(currentVst);
            } else if (currentTurnstile.contains("3")) {
                et3.setText(currentVst);
            } else if (currentTurnstile.contains("4")) {
                et4.setText(currentVst);
            }
        } else if (currentDirection.equalsIgnoreCase("west")) {
            if (currentTurnstile.contains("1")) {
                wt1.setText(currentVst);
            } else if (currentTurnstile.contains("2")) {
                wt2.setText(currentVst);
            } else if (currentTurnstile.contains("3")) {
                wt3.setText(currentVst);
            } else if (currentTurnstile.contains("4")) {
                wt4.setText(currentVst);
            }
        }
    }
}



