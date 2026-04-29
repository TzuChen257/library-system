package model;

public class Readers {
    private int id;
    private String reader_id;
    private String reader_name;
    private String national_id;
    private String birthday;
    private String address;
    private String phone;
    private String email;
    private String online_username;
    private String online_password;
    private int reward_points;
    private int reader_level;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReader_id() { return reader_id; }
    public void setReader_id(String reader_id) { this.reader_id = reader_id; }

    public String getReader_name() { return reader_name; }
    public void setReader_name(String reader_name) { this.reader_name = reader_name; }

    public String getNational_id() { return national_id; }
    public void setNational_id(String national_id) { this.national_id = national_id; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOnline_username() { return online_username; }
    public void setOnline_username(String online_username) { this.online_username = online_username; }

    public String getOnline_password() { return online_password; }
    public void setOnline_password(String online_password) { this.online_password = online_password; }

    public int getReward_points() { return reward_points; }
    public void setReward_points(int reward_points) { this.reward_points = reward_points; }

    public int getReader_level() { return reader_level; }
    public void setReader_level(int reader_level) { this.reader_level = reader_level; }
}
