package egovframework.example.sample.service;

import java.util.List;

public class WeatherVO implements Comparable<WeatherVO> {
	private WeatherType type;// 날씨 타입
	private int temp;// 기온
	private int precipitation;// 강수량
	private int snowfall;// 적설량
	private int probability;// 강수 확률
	private String date;
	private String time;
	
	public WeatherVO() {
		
	}

	public WeatherVO(List<Weather> data) {
		for(Weather weather : data) {
			Category category = weather.getCategory();
			int value = 0;
			switch(category) {
				case TMP :
					temp = Integer.parseInt(weather.getFcstValue());
					break;
				case SKY:
					value = Integer.parseInt(weather.getFcstValue());
					if(value == 1) {
						type = WeatherType.SUN;
					} else if(value == 3 || value == 4) {
						type = WeatherType.CLOUDY;
					}
					break;
				case PTY:
					value = Integer.parseInt(weather.getFcstValue());
					if(value == 1 || value == 2 || value == 5 || value == 6) {
						type = WeatherType.RAIN;
					} else if(value == 3 || value == 7) {
						type = WeatherType.SNOW;
					}
					break;
			}
			date = weather.getFcstDate();
			time = weather.getFcstTime();
		}
	}

	public WeatherType getType() {
		return type;
	}

	public void setType(WeatherType type) {
		this.type = type;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}

	public int getSnowfall() {
		return snowfall;
	}

	public void setSnowfall(int snowfall) {
		this.snowfall = snowfall;
	}

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int compareTo(WeatherVO o) {
        int dateComparison = this.date.compareTo(o.getDate());
        if (dateComparison != 0) {
            return dateComparison;
        }
        
        int timeComparison = this.time.compareTo(o.getTime());
        return timeComparison;
	}

	@Override
	public String toString() {
		return "WeatherVO [type=" + type + ", temp=" + temp + ", precipitation=" + precipitation + ", snowfall="
				+ snowfall + ", probability=" + probability + ", date=" + date + ", time=" + time + "]";
	}
	
	
}
