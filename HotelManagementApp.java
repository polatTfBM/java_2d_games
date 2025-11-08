import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Complete single-file Java Swing application that simulates a basic hotel management system.
 *
 * Features:
 *  - Dashboard summarising room and reservation statistics.
 *  - Rooms, Guests, and Reservations management tabs with tables and forms.
 *  - In-memory data storage using plain Java classes (Room, Guest, Reservation, HotelService).
 *  - Professional-looking Swing UI using only standard components.
 */
public class HotelManagementApp extends JFrame {

    private final HotelService hotelService;

    // Dashboard labels
    private final JLabel totalRoomsLabel = createStatLabel();
    private final JLabel occupiedRoomsLabel = createStatLabel();
    private final JLabel availableRoomsLabel = createStatLabel();
    private final JLabel totalReservationsLabel = createStatLabel();

    // Table models and tables
    private final RoomTableModel roomTableModel;
    private final JTable roomTable;
    private final GuestTableModel guestTableModel;
    private final JTable guestTable;
    private final ReservationTableModel reservationTableModel;

    // Sorting/filtering helpers
    private final TableRowSorter<RoomTableModel> roomSorter;
    private final TableRowSorter<GuestTableModel> guestSorter;

    // Reservation form components
    private final JComboBox<Guest> guestComboBox;
    private final JComboBox<Room> roomComboBox;
    private final JSpinner checkInSpinner;
    private final JSpinner checkOutSpinner;

    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("dd MMM yyyy");

    /**
     * Launches the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelManagementApp app = new HotelManagementApp();
            app.setVisible(true);
        });
    }

    /**
     * Constructs the main window, initialises services, sample data, and GUI components.
     */
    public HotelManagementApp() {
        super("Hotel Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        this.hotelService = new HotelService();
        this.hotelService.seedSampleData();

        this.roomTableModel = new RoomTableModel(hotelService);
        this.roomTable = new JTable(roomTableModel);
        this.roomSorter = new TableRowSorter<>(roomTableModel);
        roomTable.setRowSorter(roomSorter);

        this.guestTableModel = new GuestTableModel(hotelService);
        this.guestTable = new JTable(guestTableModel);
        this.guestSorter = new TableRowSorter<>(guestTableModel);
        guestTable.setRowSorter(guestSorter);

        this.reservationTableModel = new ReservationTableModel(hotelService);

        this.guestComboBox = new JComboBox<>();
        this.roomComboBox = new JComboBox<>();
        this.checkInSpinner = createDateSpinner();
        this.checkOutSpinner = createDateSpinner();

        configureTables();
        buildUI();
        refreshAllData();
    }

    /**
     * Utility to create stat labels with consistent styling.
     */
    private JLabel createStatLabel() {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 28));
        label.setForeground(new Color(40, 53, 147));
        return label;
    }

    /**
     * Configures table appearance for better readability.
     */
    private void configureTables() {
        roomTable.setFillsViewportHeight(true);
        guestTable.setFillsViewportHeight(true);

        // Center align table headers
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) roomTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        roomTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        guestTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        reservationTableModel.addTableModelListener(e -> refreshDashboard());
    }

    /**
     * Constructs the main tabbed UI with dashboard and management tabs.
     */
    private void buildUI() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Hotel Management Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        titleLabel.setForeground(new Color(26, 35, 126));
        getContentPane().add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tabbedPane.addTab("Dashboard", buildDashboardPanel());
        tabbedPane.addTab("Rooms", buildRoomsPanel());
        tabbedPane.addTab("Guests", buildGuestsPanel());
        tabbedPane.addTab("Reservations", buildReservationsPanel());

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Creates the dashboard summary panel.
     */
    private JPanel buildDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(new EmptyBorder(30, 40, 40, 40));
        panel.setBackground(new Color(238, 238, 255));

        panel.add(createStatCard("Total Rooms", totalRoomsLabel, new Color(92, 107, 192)));
        panel.add(createStatCard("Occupied Rooms", occupiedRoomsLabel, new Color(229, 57, 53)));
        panel.add(createStatCard("Available Rooms", availableRoomsLabel, new Color(67, 160, 71)));
        panel.add(createStatCard("Total Reservations", totalReservationsLabel, new Color(30, 136, 229)));

        return panel;
    }

    /**
     * Builds a coloured statistic card used in the dashboard.
     */
    private JPanel createStatCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 2, true),
                new EmptyBorder(20, 20, 20, 20))
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(accent);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Builds the rooms management tab with table and search filter.
     */
    private JPanel buildRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);
        JLabel title = new JLabel("Room List");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(title, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setToolTipText("Search rooms by number or type");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterRooms(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterRooms(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterRooms(searchField.getText());
            }
        });
        header.add(searchField, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);
        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Builds the guests management tab with table, search, and add/remove form.
     */
    private JPanel buildGuestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);
        JLabel title = new JLabel("Guest Directory");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(title, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setToolTipText("Search guests by name or ID");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterGuests(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterGuests(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterGuests(searchField.getText());
            }
        });
        header.add(searchField, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        panel.add(new JScrollPane(guestTable), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        formPanel.setOpaque(false);

        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("ID / Passport:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        JButton addButton = new JButton("Add Guest");
        addButton.setBackground(new Color(67, 160, 71));
        addButton.setForeground(Color.WHITE);
        addButton.setToolTipText("Add a new guest to the directory");

        JButton removeButton = new JButton("Remove Selected");
        removeButton.setBackground(new Color(229, 57, 53));
        removeButton.setForeground(Color.WHITE);
        removeButton.setToolTipText("Remove the selected guest");

        addButton.addActionListener(e -> {
            if (nameField.getText().isBlank() || idField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Name and ID are required", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Guest guest = new Guest(nameField.getText().trim(), idField.getText().trim(),
                    phoneField.getText().trim(), emailField.getText().trim());
            hotelService.addGuest(guest);
            guestTableModel.fireTableDataChanged();
            populateGuestCombo();
            clearFields(nameField, idField, phoneField, emailField);
        });

        removeButton.addActionListener(e -> {
            int selectedRow = guestTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a guest to remove", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int modelIndex = guestTable.convertRowIndexToModel(selectedRow);
            Guest guest = hotelService.getGuests().get(modelIndex);
            if (hotelService.hasActiveReservation(guest)) {
                JOptionPane.showMessageDialog(this, "Cannot remove guest with existing reservations", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            hotelService.removeGuest(guest);
            guestTableModel.fireTableDataChanged();
            populateGuestCombo();
        });

        formPanel.add(addButton);
        formPanel.add(removeButton);

        panel.add(formPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds the reservations tab which allows creating new reservations.
     */
    private JPanel buildReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Manage Reservations");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(new JLabel("Guest:"), gbc);
        gbc.gridx = 1;
        guestComboBox.setPreferredSize(new Dimension(250, 25));
        formPanel.add(guestComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 1;
        roomComboBox.setPreferredSize(new Dimension(200, 25));
        formPanel.add(roomComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Check-in:"), gbc);
        gbc.gridx = 1;
        formPanel.add(checkInSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Check-out:"), gbc);
        gbc.gridx = 1;
        formPanel.add(checkOutSpinner, gbc);

        JButton createButton = new JButton("Create Reservation");
        createButton.setBackground(new Color(30, 136, 229));
        createButton.setForeground(Color.WHITE);
        createButton.setToolTipText("Create reservation for selected guest and room");

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(createButton, gbc);

        createButton.addActionListener(e -> createReservation());

        panel.add(formPanel, BorderLayout.WEST);

        JTable reservationTable = new JTable(reservationTableModel);
        reservationTable.setFillsViewportHeight(true);
        reservationTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        panel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);

        // Update available rooms whenever dates change
        ChangeListenerAdapter changeListenerAdapter = new ChangeListenerAdapter(this::populateAvailableRooms);
        checkInSpinner.addChangeListener(changeListenerAdapter);
        checkOutSpinner.addChangeListener(changeListenerAdapter);

        return panel;
    }

    /**
     * Converts Date from spinner to LocalDate.
     */
    private LocalDate getDateFromSpinner(JSpinner spinner) {
        Date date = (Date) spinner.getValue();
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Creates a date spinner configured for LocalDate selection.
     */
    private JSpinner createDateSpinner() {
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd MMM yyyy");
        spinner.setEditor(editor);
        return spinner;
    }

    /**
     * Handles reservation creation flow including validation and confirmation dialog.
     */
    private void createReservation() {
        Guest guest = (Guest) guestComboBox.getSelectedItem();
        Room room = (Room) roomComboBox.getSelectedItem();
        LocalDate checkIn = getDateFromSpinner(checkInSpinner);
        LocalDate checkOut = getDateFromSpinner(checkOutSpinner);

        if (guest == null || room == null) {
            JOptionPane.showMessageDialog(this, "Guest and room must be selected", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!checkOut.isAfter(checkIn)) {
            JOptionPane.showMessageDialog(this, "Check-out must be after check-in", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!hotelService.isRoomAvailable(room, checkIn, checkOut)) {
            JOptionPane.showMessageDialog(this, "Room is not available for selected dates", "Validation", JOptionPane.WARNING_MESSAGE);
            populateAvailableRooms();
            return;
        }

        String message = String.format("Create reservation for %s in %s from %s to %s?", guest.getName(),
                room.getNumber(), checkIn.format(DISPLAY_DATE), checkOut.format(DISPLAY_DATE));
        int confirm = JOptionPane.showConfirmDialog(this, message, "Confirm Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) {
            return;
        }

        hotelService.createReservation(new Reservation(guest, room, checkIn, checkOut));
        reservationTableModel.fireTableDataChanged();
        roomTableModel.fireTableDataChanged();
        populateAvailableRooms();
        refreshDashboard();
        JOptionPane.showMessageDialog(this, "Reservation successfully created", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates dashboard and combo boxes when data changes.
     */
    private void refreshAllData() {
        populateGuestCombo();
        populateAvailableRooms();
        refreshDashboard();
    }

    /**
     * Repopulates guest combo box based on current guest list.
     */
    private void populateGuestCombo() {
        guestComboBox.removeAllItems();
        for (Guest guest : hotelService.getGuests()) {
            guestComboBox.addItem(guest);
        }
    }

    /**
     * Populates room combo box with rooms available for current date selection.
     */
    private void populateAvailableRooms() {
        LocalDate checkIn = getDateFromSpinner(checkInSpinner);
        LocalDate checkOut = getDateFromSpinner(checkOutSpinner);
        List<Room> available = hotelService.findAvailableRooms(checkIn, checkOut);
        roomComboBox.removeAllItems();
        for (Room room : available) {
            roomComboBox.addItem(room);
        }
    }

    /**
     * Updates dashboard statistics labels.
     */
    private void refreshDashboard() {
        totalRoomsLabel.setText(String.valueOf(hotelService.getRooms().size()));
        occupiedRoomsLabel.setText(String.valueOf(hotelService.countRoomsByStatus(RoomStatus.OCCUPIED)));
        availableRoomsLabel.setText(String.valueOf(hotelService.countRoomsByStatus(RoomStatus.AVAILABLE)));
        totalReservationsLabel.setText(String.valueOf(hotelService.getReservations().size()));
    }

    /**
     * Filters rooms based on search text.
     */
    private void filterRooms(String query) {
        if (query == null || query.isBlank()) {
            roomSorter.setRowFilter(null);
            return;
        }
        roomSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 0, 1));
    }

    /**
     * Filters guests based on search text.
     */
    private void filterGuests(String query) {
        if (query == null || query.isBlank()) {
            guestSorter.setRowFilter(null);
            return;
        }
        guestSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 0, 1));
    }

    /**
     * Clears input fields.
     */
    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    // ----------------- Data Model Classes -----------------

    /**
     * Represents the possible room statuses in the hotel.
     */
    private enum RoomStatus {
        AVAILABLE, OCCUPIED, CLEANING
    }

    /**
     * Room entity storing number, type, price, and status.
     */
    private static class Room {
        private final String number;
        private final String type;
        private final double price;
        private RoomStatus status;

        Room(String number, String type, double price, RoomStatus status) {
            this.number = number;
            this.type = type;
            this.price = price;
            this.status = status;
        }

        public String getNumber() {
            return number;
        }

        public String getType() {
            return type;
        }

        public double getPrice() {
            return price;
        }

        public RoomStatus getStatus() {
            return status;
        }

        public void setStatus(RoomStatus status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return number + " - " + type;
        }
    }

    /**
     * Guest entity storing personal details.
     */
    private static class Guest {
        private final String name;
        private final String idDocument;
        private final String phone;
        private final String email;

        Guest(String name, String idDocument, String phone, String email) {
            this.name = name;
            this.idDocument = idDocument;
            this.phone = phone;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getIdDocument() {
            return idDocument;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return name + " (" + idDocument + ")";
        }
    }

    /**
     * Reservation entity linking guest, room, and dates.
     */
    private static class Reservation {
        private final Guest guest;
        private final Room room;
        private final LocalDate checkIn;
        private final LocalDate checkOut;

        Reservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
            this.guest = guest;
            this.room = room;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
        }

        public Guest getGuest() {
            return guest;
        }

        public Room getRoom() {
            return room;
        }

        public LocalDate getCheckIn() {
            return checkIn;
        }

        public LocalDate getCheckOut() {
            return checkOut;
        }
    }

    /**
     * Core service handling in-memory data and business logic.
     */
    private static class HotelService {
        private final List<Room> rooms = new ArrayList<>();
        private final List<Guest> guests = new ArrayList<>();
        private final List<Reservation> reservations = new ArrayList<>();

        public List<Room> getRooms() {
            return rooms;
        }

        public List<Guest> getGuests() {
            return guests;
        }

        public List<Reservation> getReservations() {
            return reservations;
        }

        public void addGuest(Guest guest) {
            guests.add(guest);
        }

        public void removeGuest(Guest guest) {
            guests.remove(guest);
        }

        public boolean hasActiveReservation(Guest guest) {
            return reservations.stream().anyMatch(r -> r.getGuest().equals(guest));
        }

        public void createReservation(Reservation reservation) {
            reservations.add(reservation);
            reservation.getRoom().setStatus(RoomStatus.OCCUPIED);
        }

        public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
            if (room.getStatus() == RoomStatus.CLEANING) {
                return false;
            }
            List<Reservation> roomReservations = reservations.stream()
                    .filter(r -> r.getRoom().equals(room))
                    .collect(Collectors.toList());
            for (Reservation reservation : roomReservations) {
                if (datesOverlap(checkIn, checkOut, reservation.getCheckIn(), reservation.getCheckOut())) {
                    return false;
                }
            }
            return room.getStatus() == RoomStatus.AVAILABLE;
        }

        private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
            return !(end1.isEqual(start2) || end1.isBefore(start2) || start1.isEqual(end2) || start1.isAfter(end2));
        }

        public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
            return rooms.stream()
                    .filter(room -> isRoomAvailable(room, checkIn, checkOut))
                    .collect(Collectors.toList());
        }

        public long countRoomsByStatus(RoomStatus status) {
            return rooms.stream().filter(room -> room.getStatus() == status).count();
        }

        public void seedSampleData() {
            if (!rooms.isEmpty()) {
                return;
            }

            rooms.add(new Room("101", "Standard", 120.0, RoomStatus.AVAILABLE));
            rooms.add(new Room("102", "Standard", 120.0, RoomStatus.OCCUPIED));
            rooms.add(new Room("201", "Deluxe", 180.0, RoomStatus.AVAILABLE));
            rooms.add(new Room("202", "Deluxe", 180.0, RoomStatus.CLEANING));
            rooms.add(new Room("301", "Suite", 260.0, RoomStatus.AVAILABLE));

            guests.add(new Guest("Alice Carter", "P123456", "+1 555 1234", "alice@example.com"));
            guests.add(new Guest("Brian Taylor", "P987654", "+1 555 8765", "brian@example.com"));
            guests.add(new Guest("Cindy Lopez", "P456789", "+1 555 2468", "cindy@example.com"));

            createReservation(new Reservation(guests.get(0), rooms.get(1), LocalDate.now(), LocalDate.now().plusDays(2)));
        }
    }

    // ----------------- Table Model Implementations -----------------

    private static class RoomTableModel extends AbstractTableModel {
        private final HotelService hotelService;
        private final String[] columns = {"Number", "Type", "Price", "Status"};

        RoomTableModel(HotelService hotelService) {
            this.hotelService = hotelService;
        }

        @Override
        public int getRowCount() {
            return hotelService.getRooms().size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Room room = hotelService.getRooms().get(rowIndex);
            return switch (columnIndex) {
                case 0 -> room.getNumber();
                case 1 -> room.getType();
                case 2 -> String.format("$%.2f", room.getPrice());
                case 3 -> room.getStatus();
                default -> null;
            };
        }
    }

    private static class GuestTableModel extends AbstractTableModel {
        private final HotelService hotelService;
        private final String[] columns = {"Name", "ID / Passport", "Phone", "Email"};

        GuestTableModel(HotelService hotelService) {
            this.hotelService = hotelService;
        }

        @Override
        public int getRowCount() {
            return hotelService.getGuests().size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Guest guest = hotelService.getGuests().get(rowIndex);
            return switch (columnIndex) {
                case 0 -> guest.getName();
                case 1 -> guest.getIdDocument();
                case 2 -> guest.getPhone();
                case 3 -> guest.getEmail();
                default -> null;
            };
        }
    }

    private static class ReservationTableModel extends AbstractTableModel {
        private final HotelService hotelService;
        private final String[] columns = {"Guest", "Room", "Check-in", "Check-out"};

        ReservationTableModel(HotelService hotelService) {
            this.hotelService = hotelService;
        }

        @Override
        public int getRowCount() {
            return hotelService.getReservations().size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Reservation reservation = hotelService.getReservations().get(rowIndex);
            return switch (columnIndex) {
                case 0 -> reservation.getGuest().getName();
                case 1 -> reservation.getRoom().getNumber();
                case 2 -> reservation.getCheckIn().format(DISPLAY_DATE);
                case 3 -> reservation.getCheckOut().format(DISPLAY_DATE);
                default -> null;
            };
        }
    }

    /**
     * Utility change listener adapter for lambda usage.
     */
    private static class ChangeListenerAdapter implements javax.swing.event.ChangeListener {
        private final Runnable runnable;

        ChangeListenerAdapter(Runnable runnable) {
            this.runnable = Objects.requireNonNull(runnable);
        }

        @Override
        public void stateChanged(javax.swing.event.ChangeEvent e) {
            runnable.run();
        }
    }
}
