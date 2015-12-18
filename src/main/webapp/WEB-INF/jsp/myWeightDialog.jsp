<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Schließen">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Meine Gewichtung</h4>
			</div>
			<form action="/schwarmtabelle/myWeight" method="get">
				<div class="modal-body">
					<div class="form-group">
						<label for="exampleInputEmail1">KGV</label>
						<select class="form-control" name="pe">
							<option value="3">3 sehr wichtig</option>
							<option value="2">2 wichtig</option>
							<option value="1">1 normal</option>
							<option value="0">0 unwichtig</option>
						</select>
					</div>
					<div class="form-group">
						<label for="exampleInputEmail1">Payback-Time</label>
						<select class="form-control" name="payback">
							<option value="3">3 sehr wichtig</option>
							<option value="2">2 wichtig</option>
							<option value="1">1 normal</option>
							<option value="0">0 unwichtig</option>
						</select>
					</div>
					<div class="form-group">
						<label for="exampleInputEmail1">Performance</label>
						<select class="form-control" name="performance">
							<option value="3">3 sehr wichtig</option>
							<option value="2">2 wichtig</option>
							<option value="1">1 normal</option>
							<option value="0">0 unwichtig</option>
						</select>
					</div>
					<div class="form-group">
						<label for="exampleInputEmail1">Robustheit</label>
						<select class="form-control" name="robustness">
							<option value="3">3 sehr wichtig</option>
							<option value="2">2 wichtig</option>
							<option value="1">1 normal</option>
							<option value="0">0 unwichtig</option>
						</select>
					</div>
					<div class="form-group">
						<label for="exampleInputEmail1">Größe</label>
						<select class="form-control" name="size">
							<option value="3">3 sehr wichtig</option>
							<option value="2">2 wichtig</option>
							<option value="1">1 normal</option>
							<option value="0">0 unwichtig</option>
						</select>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Abbrechen</button>
					<button type="submit" class="btn btn-primary">Okay</button>
				</div>
			</form>
		</div>
	</div>
</div>
