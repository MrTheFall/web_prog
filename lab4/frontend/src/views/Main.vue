<template>
  <div class="container">
    <header class="header">
      <h1>
        <router-link to="/logout" class="header-link">Area Checker</router-link>
      </h1>
      <p>Ильин Никита | P3210 | 408698 | Вар. 413431</p>
    </header>
    <form @submit.prevent="checkPoint" class="controls">
      <div>
        <label for="x" class="controls-label">X:</label>
        <select v-model="x" id="x" required class="controls-input">
          <option v-for="value in xValues" :key="value" :value="value">{{ value }}</option>
        </select>
      </div>
      <div>
        <label for="y" class="controls-label">Y:</label>
        <input
          v-model.number="y"
          id="y"
          required
          type="number"
          max="5"
          min="-3"
          step="any"
          class="controls-input"
          @input="limitYInput"
        />
      </div>
      <div class="checkbox-group">
        <label>R:</label>
        <label v-for="value in rValues" :key="value">
          <input type="radio" v-model.number="r" :value="value" required />
          {{ value }}
        </label>
      </div>
      <button type="submit" class="submit-button">Check Point</button>
    </form>
    <canvas id="canvas" width="400" height="400" @click="onCanvasClick"></canvas>
    <div id="result" class="result">{{ resultMessage }}</div>
    <table id="resultsTable" class="data-table">
      <thead>
        <tr>
          <th>№</th>
          <th>X</th>
          <th>Y</th>
          <th>R</th>
          <th>Result</th>
          <th>Curr. time</th>
          <th>Exec. time</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="result in results" :key="result.id">
          <td>{{ result.id }}</td>
          <td>{{ result.x }}</td>
          <td>{{ result.y }}</td>
          <td>{{ result.r }}</td>
          <td>{{ result.hit ? 'Inside' : 'Outside' }}</td>
          <td>{{ result.date }}</td>
          <td>{{ result.scriptTime }}</td>
        </tr>
      </tbody>
    </table>
    <p id="floatingMessage" class="floating-message" v-if="errorMessage">
      {{ errorMessage }}
    </p>
  </div>
</template>


<script>
export default {
  name: 'Main',
  data() {
    return {
      x: null,
      y: null,
      r: 1,
      xValues: [-5, -4, -3, -2, -1, 0, 1, 2, 3],
      rValues: [1, 1.5, 2, 2.5, 3],
      results: [],
      resultMessage: '',
      errorMessage: '',
      canvas: null,
      ctx: null,
    };
  },
  mounted() {
    this.canvas = document.getElementById('canvas');
    this.ctx = this.canvas.getContext('2d');
    this.updateCanvas();
  },
  watch: {
    r() {
      this.drawCoordinateSystem(this.r);
      this.drawAreas(this.r);
      this.drawAllPoints(this.r);
    }
  },
  methods: {
    limitYInput() {
      if (this.y > 5) this.y = 5;
      if (this.y < -3) this.y = -3;
      if (this.y !== null) {
        const floatPart = this.y.toString().split('.')[1];
        if (floatPart && floatPart.length > 15) {
          this.y = parseFloat(this.y).toFixed(15);
        }
      }
    },
    drawCoordinateSystem(R) {
      const width = this.canvas.width;
      const height = this.canvas.height;    
      this.ctx.clearRect(0, 0, width, height);
      const scale = width / (4 * R);

      this.ctx.beginPath();
      this.ctx.moveTo(0, height / 2);
      this.ctx.lineTo(width, height / 2);
      this.ctx.moveTo(width / 2, 0);
      this.ctx.lineTo(width / 2, height);
      this.ctx.stroke();

      this.ctx.fillStyle = "rgba(0, 0, 0, 0.8)";
      const markings = [-R, -R / 2, R / 2, R];
      markings.forEach(mark => {
        const x = width / 2 + mark * scale;
        const y = height / 2 - mark * scale;

        this.ctx.beginPath();
        this.ctx.moveTo(x, height / 2 - 5);
        this.ctx.lineTo(x, height / 2 + 5);
        this.ctx.stroke();
        this.ctx.fillText(mark === 0 ? "" : mark.toString(), x, height / 2 + 20);

        this.ctx.beginPath();
        this.ctx.moveTo(width / 2 - 5, y);
        this.ctx.lineTo(width / 2 + 5, y);
        this.ctx.stroke();
        this.ctx.fillText(mark === 0 ? "" : mark.toString(), width / 2 - 20, y);
      });
      this.ctx.fillText("X", width - 10, height / 2 - 20);
      this.ctx.fillText("Y", width / 2 + 20, 10);
    },
    drawAreas(R) {
      const width = this.canvas.width;
      const height = this.canvas.height;
      const scale = width / (4 * R);

      this.ctx.fillStyle = "rgba(0, 0, 255, 0.3)";
      this.ctx.fillRect(width / 2 - R * scale, height / 2 - (R / 2) * scale, R * scale, (R / 2) * scale);

      this.ctx.beginPath();
      this.ctx.moveTo(width / 2, height / 2);
      this.ctx.lineTo(width / 2 + R * scale, height / 2);
      this.ctx.lineTo(width / 2, height / 2 + (R / 2) * scale);
      this.ctx.closePath();
      this.ctx.fill();

      this.ctx.beginPath();
      this.ctx.moveTo(width / 2, height / 2);
      this.ctx.arc(width / 2, height / 2, R * scale, -Math.PI / 2, 0);
      this.ctx.closePath();
      this.ctx.fill();
    },
    async updateCanvas() {
      // Reset results array before fetching new data
      if (!this.r) {
        this.drawCoordinateSystem(1);
        this.drawAreas(1);
        return;
      }

      // Fetch the hits data
      try {
        const response = await this.$axios.get('/api/points/get');

        // Assuming the response data is an array of hit objects
        const hitsData = response.data; // [{}, {}] format expected

        // Parse the received data
        this.results = hitsData.map(hit => {
          return {
            id: parseInt(hit.id), // Ensure ID is parsed as an integer
            x: parseFloat(hit.X), // Convert string to float
            y: parseFloat(hit.Y), // Convert string to float
            r: parseFloat(hit.R), // Convert string to float
            scriptTime: hit.script_time,
            date: hit.date,
            hit: hit.status === 'true' // Convert string 'true'/'false' to boolean
          };
        });

        // Now you can proceed to draw the canvas with the current r value
        this.drawCoordinateSystem(this.r);
        this.drawAreas(this.r);
        this.drawAllPoints(this.r);
      } catch (error) {
        // Handle errors
        if (error.response && error.response.status === 401) {
          // Redirect to /auth on 401 Unauthorized response
          this.$router.push('/auth');
        } else {
          console.error('Failed to fetch hits data:', error);
          this.showError('Failed to load hit data. Please try again.');
        }
      }

    }
,
    drawPoint(x, y, pointR, currentR) {
      const width = this.canvas.width;
      const height = this.canvas.height; 
      const scale = width / (4 * pointR);
      const ratio = pointR / currentR;

      const hit = this.isPointInsideArea(x, y, currentR);
      this.ctx.fillStyle = hit ? "green" : "red";

      this.ctx.beginPath();
      this.ctx.arc(width / 2 + x * scale * ratio, height / 2 - y * scale * ratio, 3, 0, 2 * Math.PI);
      this.ctx.fill();
    },
    drawAllPoints(currentR) {
      this.results.forEach(result => {
        this.drawPoint(result.x, result.y, result.r, currentR);
      });
    },
    isPointInsideArea(x, y, R) {
      const insideRectangle = x >= -R && x <= 0 && y <= R / 2 && y >= 0;
      const insideTriangle = x >= 0 && y <= 0 && y >= x / 2 - R / 2;
      const insideQuarterCircle = x >= 0 && y >= 0 && (x * x + y * y <= R * R);

      return insideRectangle || insideTriangle || insideQuarterCircle;
    },
    onCanvasClick(event) {
      const rect = this.canvas.getBoundingClientRect();
      if (!this.r) {
        this.showError("You have to choose R first.");
        return;
      }
      const r = this.r;
      let x = ((event.clientX - rect.left) / this.canvas.width) * (4 * r) - 2 * r; 
      const y = -((event.clientY - rect.top) / this.canvas.height) * (4 * r) + 2 * r; 
      x = this.findNearestX(x);

      this.x = x;
      this.y = y.toFixed(2);
      this.checkPoint();
    },
    findNearestX(value) {
      const allowedXValues = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
      return allowedXValues.reduce((prev, curr) => Math.abs(curr - value) < Math.abs(prev - value) ? curr : prev);
    },
    showError(message) {
      this.errorMessage = message;
      setTimeout(() => {
        this.errorMessage = '';
      }, 1500);
    },
    async checkPoint() {
      if (this.x === null || this.y === null || this.r === null) {
        this.showError('Please make sure all fields are filled out correctly.');
        return;
      }

      try {
        const response = await this.$axios.post('/api/points/add', {
          x: this.x,
          y: this.y,
          r: this.r
        });

        // Assuming the response contains the JSON structure you've provided
        const resultData = response.data; 

        // Map the response data to the expected format
        const processedData = {
          id: parseInt(resultData.id),
          x: parseFloat(resultData.X),  // Convert string to float
          y: parseFloat(resultData.Y),  // Convert string to float
          r: parseFloat(resultData.R),  // Convert string to float
          scriptTime: resultData.script_time,
          date: resultData.date,
          hit: resultData.status === 'true' // Convert string 'true'/'false' to boolean
        };

        this.results.push(processedData);
        // Update result message based on the result
        this.resultMessage = processedData.hit ? 
          'The point is inside the area.' : 
          'The point is outside the area.';
        this.updateCanvas();
      } catch (error) {
        this.showError('Failed to check point. Please try again.');
        console.error(error);
      }
    }
  }
};
</script>



<style scoped>
/* Tablet and mobile mode */
@media (max-width: 1201px) {
    .container {
        min-width: auto;
    }

    .controls {
        display: flex;
        flex-direction: column;
        gap: 15px;
    }

    .controls .checkbox-group {
        display: flex;
        flex-wrap: wrap;
    }
}

/* Mobile mode (phones and very small devices) */
@media (max-width: 600px) {
    .container {
        min-width: auto;
        padding: 15px;
    }

    .header h1 {
        font-size: 20px;
    }

    .header p {
        font-size: 12px;
    }

    .controls label {
        font-size: 12px;
    }

    .controls select,
    .controls input[type="number"] {
        font-size: 12px;
    }

    button, .submit-button {
        font-size: 14px;
        padding: 8px;
    }

    .result {
        font-size: 14px;
    }

    #resultsTable > thead > tr > th, 
    #resultsTable > tbody > tr > td {
        padding: 4px;
    }

    /* Adjust canvas size on mobile */
    #canvas {
        width: 300px;
        height: 300px;
    }
}

</style>
