package model;

import java.time.LocalTime;

public class Messages implements Comparable<Messages>{
    private int id;
    private String reader_id;
    private int message_type;
    private String content;
    private boolean is_read;
    private String created_at;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReader_id() { return reader_id; }
    public void setReader_id(String reader_id) { this.reader_id = reader_id; }

    public int getMessage_type() { return message_type; }
    public void setMessage_type(int message_type) { this.message_type = message_type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isIs_read() { return is_read; }
    public void setIs_read(boolean is_read) { this.is_read = is_read; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
	@Override
	public int compareTo(Messages o) {
		return LocalTime.parse(this.getCreated_at()).compareTo(LocalTime.parse(o.getCreated_at()));
	}
}
